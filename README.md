# DevOps-Project

## To run a basic jenkins jobs
1. Download the mileston-1 branch

2. Run baker bake through both ansible-srv and jenkins-srv

3. Set-up Jenkins. There are two options:

3.1 Manual: 

3.1.1 [Setup jenkins](https://linuxize.com/post/how-to-install-jenkins-on-ubuntu-18-04/)
3.1.2 Setup your admin account on jenkins
3.1.3 Go to http://192.168.33.100:8080/me/configure and generate a token
3.1.4 Add that token to roles/templates/jenkins_jobs.ini.j2

3.2 Automatic installation:

3.2.1 Run ```ansible-playbook -i inventory site.yml``` by uncommenting the ```jenkins``` role. The ```jenkins``` role installs Jenkins and configures it.

NOTE: 
Jenkins login: Username and Password are ```jenkins```. Admin User Credentials: Username: ```admin```, Password: ```admin123```

4. baker ssh into the jenkins-srv

6. jenkins-jobs update jobs
