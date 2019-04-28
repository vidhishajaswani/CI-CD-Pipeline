# Setting up kubernetes for testing

--> Install Minikube/Minishift
--> run minikube start
--> go to the directory that has deployment.yaml
--> kubectl create -f deployment.yaml
--> kubectl create -f service.yaml
--> curl http://minikube:80

# DevOps-Project-Milestone 3

This is the submission by Team 5 of CSC 519 - DevOps class of Spring 2019 for the Milestone 3 of DevOps Project. The contributors in alphabetic order are:

Karthik Medidisiva  -   kmedidi

Kshittiz Kumar      -   kkumar4

Shivam Chamoli      -   schamoli

Vidhisha Jaswani    -   vjaswan

## Introduction
Use milestone_3 branch for this submission
In this milestone the following objectives were tackled.


## Replication Instructions

Fill in all credentials include AWS Secret Access Key and Access key in ansible-srv/variables.yml

1. Git clone the milestone_3 branch and set up the ansible-srv using ```baker bake```.
2. SSH into the ansible-srv using ```baker ssh```.
3. Navigate to the shared folder inside this VM i.e ```ansible-srv/```.
4. Run the playbook to setup the Jenkins EC2 Instance

```ansible-playbook -i inventory setup_jenkins.yml```

5. Run the playbook to setup the Jenkins EC2 Instance

```ansible-playbook -i inventory setup_production.yml```

6. Go inside the ~/.ssh of your local computer and create a key-pair using ```ssh-keygen -t rsa -b 4096 -C “ec2jenkins” -f ec2jenkins```.  Copy the content of ec2jenkins.pub. Now SSH into the Jenkins machine using command  ```ssh -i "jenkins.pem" ubuntu@<ip>```.  You can pick this IP from your Amazon IAM Console. Inside this jenkins machine go to the ~/.ssh folder and paste contents of public key in authorized_keys file. Check if you are able to SSH into the jenkins machine via your local machine.

7. On your local machine clone the checkbox repo [checkbox](https://github.com/ShivamChamoli/checkbox.io) and navigate into this folder and set up the prod remote as 


```$ git remote add prod vagrant@<IP of jenkins-srv>:/~/checkbox.io.git```

Make sure you did not have an existing remote prod.

Now make a commit and push to bare repository in order to have the code there 
```
touch abcd
git add .
git commit -m "first"
git push prod master
```

Make sure you see code inside checkbox.io folder kept on home of Jenkins EC2.

8. On your local machine clone the iTrust repo [iTrust](https://github.ncsu.edu/schamol/iTrust2-v4.git) and navigate into this folder and set up the prod remote as 


```$ git remote add prod vagrant@<IP of jenkins-srv>:/~/iTrust2-v4.git```

Make sure you did not have an existing remote prod.

Now make a commit and push to bare repository in order to have the code there 
```
touch abcd
git add .
git commit -m "first"
git push prod master
```

Make sure you see code inside iTrust2-v4 folder kept on home of Jenkins EC2.

9.  Now from the ansible-srv machine run command 

```
ansible-playbook -i inventory jobs.yml
```



## Deployment on EC2

1. We setup our Jenkins Server by installing all dependencies such as Maven, Ansible, MySQL, Java first. 
2. Jenkins is then installed on this server. We have used port 9999 for Jenkins since iTrust runs on 8080. You may access Jenkins and login using username ```jenkins``` and password ```jenkins```. You may change these values in variables.yml.
3. After Jenkins is setup, a new instance spins up and installs all dependencies such as nginx, mysql, node, java, mongodb. Then jobs for checkbox.io, iTrust Fuzzer,  iTrust, checkbox analysis are created. As soon as the commit is made from the checkbox code, a [file](ansible-srv/roles/create-jobs/checkbox_post_receive.j2) is invoked which is inside ```checkbox.io.git/hooks``` and for iTrust, [file](ansible-srv/roles/create-jobs/itrust_post_receive.j2 )which is kept inside ```iTrust2-v4.git/hooks```. This hook invokes the build job.
4.  When the build jobs are invoked each application is then deployed on AWS EC2 instances.



## Feature Flags

## Infrastructure and Microservice

## Something Special











## Results




## Contribution
1. **Karthik Medidisiva** :

2. **Kshittiz Kumar**: 

3. **Shivam Chamoli**: 

4. **Vidhisha Jaswani**: 
 
## Screencast
[Screencast Link](https://youtu.be/xwh4ocSP8po)

**Thank you!**
