#!/bin/bash

javadoc_propset() {
    svn propset -R svn:mime-type text/css `find apidocs/ -name "*.css"`
    svn propset -R svn:mime-type text/javascript `find apidocs/ -name "*.js"`
    svn propset -R svn:mime-type text/html `find apidocs/ -name "*.html"`
    svn propset -R svn:mime-type text/html `find apidocs/ -name "*.htm"`
    svn propset -R svn:mime-type image/x-png `find apidocs/ -name "*.png"`
    svn propset -R svn:mime-type image/gif `find apidocs/ -name "*.gif"`
    svn propset -R svn:mime-type image/jpeg `find apidocs/ -name "*.jpg"`
    svn propset -R svn:mime-type image/tiff `find apidocs/ -name "*.tif"`
    svn propset -R svn:mime-type image/tiff `find apidocs/ -name "*.tiff"`
}

die() {
        echo $1
        exit 1
}


WORKING_DIR="`pwd`"
DIR_CONTAINING_THIS_SCRIPT="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

cd "$DIR_CONTAINING_THIS_SCRIPT" || die "Could not locate directory containing script"

if [ -d "apidocs/" ]; then
    echo "Removing existing JavaDocs from SVN"
    svn remove apidocs/ || die "Could not remove existing JavaDocs from SVN"
    svn ci apidocs/ -m "Updating JavaDocs [Step 1]" || die "Could not SVN commit removal of JavaDocs"
fi

echo "Re-generating javadocs"
if [ -d "/Library/Java/JavaVirtualMachines/1.7.0.jdk/Contents/Home/" ]; then
    echo "INFO: Located Java 7, generating prettier JavaDocs with Java 7"
    export JAVA_HOME=/Library/Java/JavaVirtualMachines/1.7.0.jdk/Contents/Home/
else
    echo "WARNING: Could not locate Java 7, using default JDK to generate ugly JavaDocs"
fi
cd ../trunk || die "Could not locate trunk directory"
mvn clean javadoc:javadoc || die "Could not generate JavaDocs"


echo "Copying JavaDocs to javadoc directory"
cp -R target/site/apidocs ../javadoc/ || die "Could not copy JavaDocs to SVN directory"


echo "Adding new JavaDocs to SVN"
cd "$DIR_CONTAINING_THIS_SCRIPT" || die "Could not locate javadoc directory"
svn add apidocs/ || die "Could not add new JavaDocs to SVN"
echo "Setting SVN MIME types on JavaDocs"
javadoc_propset 2> /dev/null
echo "Committing new JavaDocs in 10 seconds - CTRL-C to cancel"
sleep 10 || die "SVN commit cancelled"
echo "Committing new JavaDocs to SVN"
svn ci apidocs/ -m "Updating JavaDocs [Step 2]" || die "Could not commit new JavaDocs to SVN"


cd "$WORKING_DIR" || die "Could not change back to working directory"

