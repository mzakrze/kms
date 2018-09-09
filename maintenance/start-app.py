# 1. rozszyfruj credentiale do bazy
# 2. przygotuje application.properties
# 3. docker-compose start

import os
import sys
import configparser


os.chdir('../')
PROJECT_ROOT = os.getcwd()

with open(PROJECT_ROOT + '/VERSION') as f:
    APP_VERSION = f.read().strip()

if os.popen('docker container ls | grep kms:' + APP_VERSION).read() != '':
    print('Already running( localhost:9898 ). Exiting.')
    sys.exit()


parser = configparser.ConfigParser()
parser.read(PROJECT_ROOT + '/be/src/main/resources/application.properties')
params = {section: dict(parser.items(section)) for section in parser.sections()}['kms_application_properties']
os.system('gpg2 ' + PROJECT_ROOT + '/maintenance/deploy/local-machine/secrets/database.properties.gpg')
prod_db_credentials_parser = configparser.ConfigParser()
prod_db_credentials_parser.read(PROJECT_ROOT + '/maintenance/deploy/local-machine/secrets/database.properties') 
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

os.chdir(PROJECT_ROOT + '/maintenance/deploy/local-machine')
os.system('docker-compose start')

os.system('rm ' + PROJECT_ROOT + '/maintenance/deploy/local-machine/secrets/database.properties')
os.system('rm ' + PROJECT_ROOT + '/maintenance/deploy/local-machine/secrets/application.properties')

# TODO - aktywne oczekiwanie aż serwer wstanie
print('Run in: localhost:9898')

