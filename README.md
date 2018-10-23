# Create new User role and Database
createuser training
createdb --encoding=UTF8 --owner=training training

# Drop User role and Database
DROPUSER training
DROPDB training
