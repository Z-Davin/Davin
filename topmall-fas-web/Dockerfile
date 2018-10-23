FROM anapsix/alpine-java

ADD ./app /usr/local/topmall

RUN chmod 777 /usr/local/topmall/start.sh

ENV APP_NAME topmall-fas-web-0.5.1-SNAPSHOT

ENV APP_BOOTSTRAP topmall.fas.web.WebApplicationBootstrap

ENTRYPOINT  ["/usr/local/topmall/start.sh"]
 


