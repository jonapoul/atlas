FROM gradle:9.1.0-jdk21

RUN apt-get update
RUN apt-get install -y graphviz curl
RUN rm -rf /var/lib/apt/lists/*

RUN curl -sSLO https://github.com/pinterest/ktlint/releases/download/1.7.1/ktlint
RUN chmod a+x ktlint
RUN mv ktlint /usr/local/bin/

WORKDIR /modular
CMD ["/bin/bash"]
