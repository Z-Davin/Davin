#!/bin/bash
set -e

cd ../

init()
{    
    WORK_DIR=$PWD

    VERSION=$(grep "<version>.*-SNAPSHOT</version>" ./pom.xml |  sed -n '2p' | sed  's#<version>##g' | sed  's#</version>##g' | sed s/[[:space:]]//g)

    REPOSITORY=$1    
    APP_NAME=$2
    APP="$2"
    IID=$3

    BASE_IMAGE="$REPOSITORY/wrapper:1.2"

    WEB_PACKAGE="$WORK_DIR/deploy/$APP-web-$VERSION-package.zip"
    DEPLOY_PACKAGE="$WORK_DIR/deploy/$APP-deployer-$VERSION-package.zip"
}

docker_build(){
    cd $WORK_DIR
    TYPE=$1
    rm -rf  $WORK_DIR/docker/$TYPE/
    mkdir  $WORK_DIR/docker/$TYPE/

    IMAGE_NAME="$REPOSITORY/$APP-$TYPE:$VERSION"

    eval "cat <<EOF
$(< $WORK_DIR/docker/Dockerfile)
EOF
" > $WORK_DIR/docker/$TYPE/Dockerfile

    unzip -o $WORK_DIR/deploy/$APP-$TYPE-$VERSION-package.zip -d ./docker/$TYPE/app/

    cd ./docker/$TYPE/  && docker build -t $IMAGE_NAME .

    docker push $IMAGE_NAME
}

helm_build(){
    cd $WORK_DIR

    rm -rf ./docker/$APP && mkdir ./docker/$APP

    cp -rf ./docker/helm/* ./docker/$APP

    tree ./docker/$APP

    if [ ! -e "$DEPLOY_PACKAGE" ]; then
		echo "remove deployer-deployment.yaml"
        rm -rf  ./docker/$APP/templates/deployer-deployment.yaml
    fi
    if [ ! -e "$WEB_PACKAGE" ]; then
		echo "remove web-deployment.yaml"
        rm -rf  ./docker/$APP/templates/web-deployment.yaml
    fi

    eval "cat <<EOF
$(< ./docker/helm/values.yaml)
EOF
" > $WORK_DIR/docker/$APP/values.yaml

    eval "cat <<EOF
$(< ./docker/helm/Chart.yaml)
EOF
" > $WORK_DIR/docker/$APP/Chart.yaml

	for f in ./docker/helm/templates/* ;do		
		render $f
	done
	
    cd $WORK_DIR/docker/

    helm package --app-version $VERSION --version $VERSION.$IID ./$APP

    rm -rf $WORK_DIR/deploy/*.tgz
    
    mv *$IID.tgz $WORK_DIR/deploy/
}

render(){
	FILE_NAME=$(echo $1 | awk -F'/' '{print $NF}');
	echo "render file: $1"
	eval "cat <<EOF
$(< ./docker/helm/templates/$FILE_NAME)
EOF
" > $WORK_DIR/docker/$APP/templates/$FILE_NAME
}

main(){
    init $@
    cd $WORK_DIR

    if [ -e "$DEPLOY_PACKAGE" ]; then
	    echo "build deploy docker"
        docker_build "deployer"
    fi

    if [ -e "$WEB_PACKAGE" ]; then
        echo "build web docker"
        docker_build "web"
    fi

    helm_build $@
}
    
main $@


#buid.sh registry.topmall.com:5000 administrator iid


