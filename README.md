# DevOps-Project-Milestone 1

This is the submission by Team 5 of CSC 519 - DevOps class of Spring 2019 for the Milestone 1 of DevOps Project. The contributors in alphabetic order are:

Karthik Medidisiva  -   kmedidi

Kshittiz Kumar      -   kkumar4

Shivam Chamoli      -   schamoli

Vidhisha Jaswani    -   vjaswan

## Introduction
In this milestone the following objectives were tackled.

1. Automatic installation of Jenkins on a remote VM
2. Automatic creation of jobs for the following applications on Jenkins:
    - checkbox.io
    - iTrust
3. Automatic triggering on builds for the jobs upon commit to the respective application's repository. These builds run tests and deploy the applications on the aforementioned remote VM
4. Using a combination of mocha/pm2, created a test script that will start and stop the express server and hit an endpoint of checkbox.io service to check its status. 

## Roles & their Description

1. Ansible: Installs Ansible on Jenkins VM.
2. Maven: Installs Maven on Jenkins VM.
3. Java: Installs Java 8 on Jenkins VM.
4. MySQL: Install MySQL and edits root password on Jenkins VM.
5. Deployfiles: Copies all files to Jenkins VM required for building the applications.
6. Jenkins: Automatically installs Jenkins on ```http://<ip_address_of_jenkins_srv>:<jenkins_port>/``` along with bypassing the user account setup and installing plugins.
7. Jobs: Creates and triggers build for checkbox.io and iTrust applications.


## Instructions for execution

## Initial Steps

1. Git clone the mileston-1 branch and set up the ansible-srv and jenkins-srv using baker bake.

2. SSH into both the machines and navigate to the shared folder /ansible-srv on the ansible-srv VM.

## Automatic installation of Jenkins

1. Execute the ```java``` and ```jenkins``` roles in site.yml using the command ```ansible-playbook -i inventory site.yml```
   NOTE:
   The ```jenkins_port``` may be set in vars/main.yml. However this is discouraged. Do NOT set to 8080 as this port is used by  iTrust. We have used port 9999.
   
   A new user with the credentials as follows is created. username: ```jenkins``` password: ```jenkins```. You may change these credentials in vars/main.yml for this role.

2. Check the installation by logging into ```http://<ip_address_of_jenkins_srv>:<jenkins_port>/```, in our case 192.168.33.100:9999. You must be able to see the log in page of Jenkins.

![jenkins](results/jenkins.png)  

## Running build job for checkbox.io

1. Comment the java and jenkins roles in site.yml and uncomment ansible, maven, mysql, deployfiles, and job roles in site.yml.
2. Create a public-private key pair on your computer using ssh-keygen and add the public SSH key to GitHub and keep in the private key under ansible-srv/roles/deployfiles/files and name it id_rsa. This is important since we need to git clone from github.ncsu.edu.
3. Inside roles/job/tasks/main.yml use only the create_checkbox_job.yml and build_checkbox_job.yml.
4. Run site.yml using the command ```ansible-playbook -i inventory site.yml```
5. Check 192.168.33.100:9999 to see checkbox.io up and running.

![checkbox.io](results/checkbox.io.png)  

##### npm test for checkbox.io

1. After checkbox.io is up and running, navigate to our cloned repo DevOps-Project on your local computer and run ```./build.sh```

2. This will run ```npm install``` and ```npm test``` which installs all dependencies, runs the express server, checks for the endpoint of checkbox.io application and returns true if the application is up and running.


![npm test](results/npmtest.png)  
##### To configure automatic execution of build job after a commit, a post-commit hook can be used with following content!
```
#!/bin/bash
curl http://192.168.33.100:9999/git/notifyCommit?url=https://github.com/ShivamChamoli/checkbox.io.git
# 192.168.33.100:9999 is the jenkins server address
# https://github.com/ShivamChamoli/checkbox.io.git is the forked repository
```
## Running build job for iTrust

1. Comment the java, jenkins, ansible, maven, and mysql roles in site.yml and uncomment deployfiles,  job roles in site.yml.
2. Inside roles/job/tasks/main.yml use only the create_itrust_job.yml and build_itrust.yml.
3. Create global credentials on Jenkins as on the below screenshot and copy the ID value and add it to roles/job/tasks/itrust_job.yml

![credentials](results/credentials.png)  

4. Add your credentials for in deployfiles/templates/email.properties.j2 for the email account.
5. Run site.yml using the command ```ansible-playbook -i inventory site.yml```
6. Check 192.168.33.100:8080/iTrust2 to see iTrust up and running.

![iTrust](results/iTrust.png)  

7. To run ```mvn clean test verify checkstyle:checkstyle``` navigate to /var/lib/jenkins/iTrust2/iTrust2

![checkstyle](results/checkstyle.png)  


##### To configure automatic execution of build job after a commit, a post-commit hook can be used with following content!
```
#!/bin/bash
curl http://192.168.33.100:9999/git/notifyCommit?url=https://github.ncsu.edu/schamol/iTrust2-v4.git
# 192.168.33.100:9999 is the jenkins server address
# https://github.ncsu.edu/schamol/iTrust2-v4.git is the forked repository
```


## Contributions

1. Karthik Medidisiva

2. Kshittiz Kumar   

3. Shivam Chamoli 

4. Vidhisha Jaswani

Setting up build jobs for checkbox.io and iTrust and automated build/deploying of checkbox.io and iTrust (along with deploying all dependencies like java, ansible, mysql, maven). Made the screencast and made contributions to ReadMe file.

## Screencast
[Screencast Link]()

**Thank you!**
