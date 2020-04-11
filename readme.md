### remote server 54.159.125.122
###ssh login
```bash
ssh -i ucareer.pem centos@54.159.125.122
update yum 
sudo yum update
sudo yum install wget
sudo yum install mysql-server

wget https://dev.mysql.com/get/mysql80-community-release-el7-3.noarch.rpm
md5sum mysql80-community-release-el7-3.noarch.rpm
sudo rpm -ivh  mysql80-community-release-el7-3.noarch.rpm 

sudo yum install mysql-server
sudo systemctl start mysqld
systemctl status mysqld
sudo grep 'temporary password' /var/log/mysqld.log
```



###before install mysql_secure_installation
####create db user password
```
openssl rand 14 -base64
mysql_secure_installation
```

### create user enable remote priv

```

CREATE USER 'ucareer'@'localhost' IDENTIFIED BY 'LSnWpGKencRi8Xj5Ar4=';
CREATE USER 'ucareer'@'%' IDENTIFIED BY 'LSnWpGKencRi8Xj5Ar4=';

GRANT ALL ON *.* TO 'ucareer'@'localhost';
GRANT ALL ON *.* TO 'ucareer'@'%';

flush privileges;
```

### Database Config:

1. MySQL (Local)
    - using port 3306;

2. Docker Compose
    - using port 3306, and configed in docker-compose file
    - if 3306 is taken, check what process is using it: 
        - sudo lsof -i:3306
    - if it's mysql, kill the server use: 
        - sudo service mysql stop
        - https://askubuntu.com/questions/696927/how-come-i-get-mysqld-service-failed-to-load-no-such-file-or-directory
        - https://tableplus.com/blog/2018/10/how-to-start-stop-restart-mysql-server.html
        - after release port 3306, cd to application root file and up docker-compose