FROM gradle:9.7.1-jdk21@sha256:c0ce93e022ea2e705332dabe090019c749356576fc8fe39c38129b2aae9ed68f

RUN apt-get update
RUN apt-get install -y graphviz libgvplugin-neato-layout8 curl

# https://d2lang.com/tour/install/
# Pinned because d2's SVG output changes between releases, and the committed samples are diffed in CI.
# renovate: datasource=github-releases depName=terrastruct/d2
ENV D2_VERSION=v0.9.0
RUN curl -fsSL https://d2lang.com/install.sh | sh -s -- --version $D2_VERSION

# SVG to PNG converters
RUN apt-get install -y imagemagick
RUN apt-get install -y inkscape
RUN apt-get install -y librsvg2-bin
RUN apt-get install -y python3 python3-pip
RUN pip3 install --break-system-packages cairosvg

# Clear cache for reduced image size
RUN apt-get clean
RUN rm -rf /var/lib/apt/lists/*

WORKDIR /atlas
CMD ["/bin/bash"]
