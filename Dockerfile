# Dockerfile with tensorflow gpu support on python3, opencv3.3
FROM tensorflow/tensorflow:latest-jupyter

WORKDIR /notebooks

RUN apt-get update
# if install libjasper-dev,need add-apt-repository,and if run add-apt-repository command,need install software-properties-common
RUN apt-get install -y software-properties-common 
RUN add-apt-repository "deb http://security.ubuntu.com/ubuntu xenial-security main"
RUN apt-get install -y libjasper-dev 

RUN sed -i "s|http://archive.ubuntu.com|http://mirrors.163.com|g" /etc/apt/sources.list && rm -Rf /var/lib/apt/lists/* && apt-get -y update && apt-get install -y \ 
    pkg-config \
    python-dev \ 
    python3-opencv \ 
    libopencv-dev \ 
    ffmpeg  \ 
    libjpeg-dev \ 
    libpng-dev \ 
    libtiff-dev \ 
    libjasper-dev \ 
    python-numpy \ 
    python-pycurl
	
# should start docker container by shell,it can help you set port and make you connect jupyter with browser
# the cmd like docker run -p 8888:8888 [container id]

# the command which build docker container is "docker build -t tensorflowCV ."
   