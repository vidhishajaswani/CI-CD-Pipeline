# DevOps-Project

## To run a basic jenkins jobs
1. Download the mileston-1 branch

2. Run baker bake through both ansible-srv and jenkins-srv

3. Set-up Jenkins. There are two options:

## Manual: 

1. [Setup jenkins](https://linuxize.com/post/how-to-install-jenkins-on-ubuntu-18-04/)

2. Setup your admin account on jenkins

3. Go to http://192.168.33.100:8080/me/configure and generate a token

4. Add the post to roles/templates/jenkins_jobs.ini.j2

5. Change the 

## Automatic installation:

1. Run ```ansible-playbook -i inventory site.yml``` by uncommenting the ```jenkins``` role. The ```jenkins``` role installs Jenkins and configures it. A new user is created.

  NOTE: 
  User Credentials: Username: ```jenkins```, Password: ```jenkins```

2. baker ssh into the jenkins-srv

3. jenkins-jobs update jobs

## Running build job for checkbox.io

1. Run site.yml with build_checkbox.yml
2. Check 192.168.33.100:8080

##### To configure automatic execution of build job after a commit, a post-commit hook can be used with following content!
```
#!/bin/bash
curl http://<Jenkins server>/git/notifyCommit?url=<URL of github repository>
```
