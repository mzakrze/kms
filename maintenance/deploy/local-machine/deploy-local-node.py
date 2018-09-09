# 1. pobierz jako argument nazwę pliku ze skryptem migracyjnym
# 2. pobierz wersję z pliku VERSION
# 3. zbuduj frontend
# 4. skopiuj frontend do zasobów aplikacji
# 5. wczytaj application.properties -> podmień konf -> wpisz do katalogu secrets
# 6. zbuduj jar'a 
# 7. zbuduj obraz, otaguj
# 8. przygotuj skrypt migracyjny
# 9. zastopuj aplikację
# 10. uruchom skrypt migracyjny
# 11. uruchom nową wersję

import os
import sys
import psycopg2
import configparser

os.chdir('../../..')
PROJECT_ROOT = os.getcwd()

# 1
if len(sys.argv) < 2:
    print('plz specify migration script file name')
    sys.exit()
MIGRATION_FILENAME = sys.argv[1]

# 2
with open(PROJECT_ROOT + '/VERSION') as f:
    APP_VERSION = f.read().strip()
    print('APP_VERSION', APP_VERSION)

# 3
os.chdir(PROJECT_ROOT + '/fe')
os.system('make build-frontend')

# 4
# TODO - zmusić Spring'a aby brał resource'y przy budowaniu z danego katalogu, wtedy można będze gdzieś wynieść te artefakty
os.system('for i in $(ls dist); do cp "dist/$i" ' + PROJECT_ROOT + '/be/src/main/resources/frontend_application; done;')
os.system('mv ' + PROJECT_ROOT + '/be/src/main/resources/frontend_application/bundle.js ' + PROJECT_ROOT + '/be/src/main/resources/frontend_application/app-bundle.js')

# 5
os.chdir(PROJECT_ROOT + '/be')
parser = configparser.ConfigParser()
parser.read('src/main/resources/application.properties')
params = {section: dict(parser.items(section)) for section in parser.sections()}['kms_application_properties']
os.system('gpg2 ../maintenance/deploy/local-machine/secrets/database.properties.gpg')
prod_db_credentials_parser = configparser.ConfigParser()
prod_db_credentials_parser.read('../maintenance/deploy/local-machine/secrets/database.properties') 
prod_db_credentials = {section: dict(prod_db_credentials_parser.items(section)) for section in prod_db_credentials_parser.sections()}['kms_local_prod_database']
# FIXME adres ip jest adresem hosta z wewnątrz dockera, należy dostarczyć to jakoś sensowniej bo może nie zawsze działać
params['spring.datasource.url'] = 'jdbc:postgresql:/172.17.0.1/' + prod_db_credentials['dbname']
params['spring.datasource.username'] = prod_db_credentials['user']
params['spring.datasource.password'] = prod_db_credentials['password']
params['spring.jpa.hibernate.ddl-auto'] = 'validate'
params['application.version'] = APP_VERSION
parser['kms_application_properties'] = params
with open(PROJECT_ROOT + '/maintenance/deploy/local-machine/secrets/application.properties', 'w') as configfile:
    parser.write(configfile)

# 6
# FIXME - poprawienie testów
os.system('./gradlew build -x test')

# 7
os.chdir(PROJECT_ROOT + '/be')
image_name = "kms:" + APP_VERSION
jar_file = "build/libs/kms-" + APP_VERSION + ".jar"
os.system('docker build --build-arg JAR_FILE=' + jar_file + ' --tag ' + image_name + ' .')

# 8
os.chdir(PROJECT_ROOT + '/maintenance/deploy/updates')
with open(MIGRATION_FILENAME) as f:
    full_migration_script = f.read()

# 9
os.chdir(PROJECT_ROOT + '/maintenance/deploy/local-machine')
os.system('docker-compose stop')

# 10 
os.chdir(PROJECT_ROOT + '/maintenance/deploy/local-machine/secrets')
prod_db_credentials['host'] = 'localhost'
try:
    with psycopg2.connect(**prod_db_credentials) as conn, conn.cursor() as cur:
        conn.tpc_begin('whatever')
        cursor = conn.cursor()
        cursor.execute(full_migration_script)
        conn.tpc_commit()
except psycopg2.ProgrammingError as e:
    print('ERROR while running migration script. Rolling back transaction and exiting ...')
    print('ALERT: production is most likely currenty down')
    conn.tpc_rollback()
    sys.exit(1)

# 11 
os.chdir(PROJECT_ROOT + '/maintenance/deploy/local-machine')
os.system('docker-compose create')
os.system('docker-compose start')

# no i posprzątaj po sobie
os.system('rm ' + PROJECT_ROOT + '/maintenance/deploy/local-machine/secrets/database.properties')
os.system('rm ' + PROJECT_ROOT + '/maintenance/deploy/local-machine/secrets/application.properties')