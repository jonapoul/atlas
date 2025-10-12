FROM gradle:9.1.0-jdk21

RUN apt-get update
RUN apt-get install -y graphviz curl
RUN rm -rf /var/lib/apt/lists/*

# https://d2lang.com/tour/install/
RUN curl -fsSL https://d2lang.com/install.sh | sh -s --

WORKDIR /atlas
CMD ["/bin/bash"]
