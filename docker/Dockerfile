FROM gradle:9.1.0-jdk21

RUN apt-get update
RUN apt-get install -y graphviz
RUN rm -rf /var/lib/apt/lists/*

WORKDIR /modular
CMD ["/bin/bash"]
