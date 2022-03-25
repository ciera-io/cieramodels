FROM ubuntu:20.04

# Metadata
LABEL maintainer="levi@roxsoftware.com"
LABEL version="v3.0.0-beta1"

# Install dependencies
RUN export DEBIAN_FRONTEND=noninteractive && apt-get update && apt-get install -y maven python3 python3-pip
RUN ln -s /usr/bin/python3 /usr/bin/python
RUN python -m pip install pyxtuml

# Create user
RUN groupadd -g 1000 xtuml
RUN useradd -u 1000 -g xtuml -d /home/xtuml -m xtuml -s /bin/bash
RUN echo 'xtuml  ALL=(ALL:ALL)   NOPASSWD:ALL' >> /etc/sudoers
USER xtuml

# Clone the Ciera repo
ADD --chown=xtuml:xtuml . /home/examples

# Set up projects
WORKDIR /home/examples/MicrowaveOven
RUN mvn install
WORKDIR /home/examples/GPS
RUN mvn install
WORKDIR /home/examples/HelloWorld
RUN mvn install
WORKDIR /home/examples/calculator
RUN mvn install

# Set working directory
WORKDIR /home/examples
