#References : https://boto3.amazonaws.com/v1/documentation/api/latest/guide/ec2-examples.html

import boto3
import os
import sys
access_key=os.environ.get('AWS_ACCESS_KEY_ID')
secret_access_key=os.environ.get('AWS_SECRET_ACCESS_KEY')


ec2 = boto3.resource('ec2', region_name='us-east-1',aws_access_key_id=access_key, aws_secret_access_key=secret_access_key)
path='/ansible-srv/roles/aws/templates/'
awskey=sys.argv[1]
filename=awskey+'.pem'
# Create a Key Pair that will be linked to our Instance
#outfile = open(jenkins-key,'w')
outfile = open(filename,'w')
key_pair = ec2.create_key_pair(KeyName=awskey)
KeyPairOut = str(key_pair.key_material)
outfile.write(KeyPairOut)

# Create Virtual Private Cloud
vpc = ec2.create_vpc(CidrBlock='10.0.0.0/24')

subnet = vpc.create_subnet(CidrBlock='10.0.0.0/25')
gateway = ec2.create_internet_gateway()
gateway.attach_to_vpc(VpcId=vpc.id)
route_table = vpc.create_route_table()
route = route_table.create_route(
    DestinationCidrBlock='0.0.0.0/0',
    GatewayId=gateway.id
)
route_table.associate_with_subnet(SubnetId=subnet.id)


#A security group acts as a virtual firewall that controls the traffic for one or more instances. 
sec = ec2.create_security_group(
    GroupName='aws-ssh', Description='vidhisha sec group', VpcId=vpc.id)
sec_group=sec.authorize_ingress(
        IpPermissions=[
            {'IpProtocol': 'tcp',
             'FromPort': 22,
             'ToPort': 22,
             'IpRanges': [{'CidrIp': '0.0.0.0/0'}]},
             {'IpProtocol': 'tcp',
             'FromPort': 9999,
             'ToPort': 9999,
             'IpRanges': [{'CidrIp': '0.0.0.0/0'}]},
             {'IpProtocol': 'tcp',
             'FromPort': 80,
             'ToPort': 80,
             'IpRanges': [{'CidrIp': '0.0.0.0/0'}]}
        ]
)

# Finally create instance by giving the Image ID and Instance Type
instances = ec2.create_instances(
    ImageId='ami-0565af6e282977273', InstanceType='t2.micro', MaxCount=1, MinCount=1,KeyName=awskey,
    NetworkInterfaces=[{'SubnetId': subnet.id, 'DeviceIndex': 0, 'AssociatePublicIpAddress': True, 'Groups': [sec.group_id]}])

#print(instances)
print("Instance created")




instances[0].wait_until_running()

instances[0].load()
mytags = [{
       "Key" : awskey, 
       "Value": awskey
    } 
    ]
ec2.create_tags(
            Resources = [instances[0].id ],
            Tags= mytags
           )
instances[0].wait_until_running()

instances[0].load()
print(instances[0].public_ip_address)






f = open("/ansible-srv/inventory", "w")
f.write("[jenkins]\n")
f.write("jenkins-srv ansible_host="+instances[0].public_ip_address+" ansible_ssh_user=ubuntu ansible_python_interpreter=/usr/bin/python3 ansible_ssh_private_key_file="+path+filename)






