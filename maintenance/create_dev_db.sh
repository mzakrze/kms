
sudo -H -u postgres psql -c "CREATE USER kms_dev WITH PASSWORD 'kms_dev'"
sudo -H -u postgres psql -c "CREATE DATABASE kms_dev"
sudo -H -u postgres psql -c "GRANT ALL PRIVILEGES ON DATABASE kms_dev TO kms_dev"
