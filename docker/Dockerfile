#FROM registry.topmall.com:5000/wrapper:1.2
FROM $BASE_IMAGE

MAINTAINER hong.l@belle.com.cn

WORKDIR /usr/local/topmall

ADD app /usr/local/topmall/app

ADD app/conf/app.conf /usr/local/topmall/app/

#ADD args.conf /usr/local/topmall/wrapper/conf/

ENTRYPOINT ["./wrapper/bin/StartWrapper","console"]
