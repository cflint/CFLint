see
https://github.com/denuno/cfml.parsing.git
and
https://github.com/denuno/cfml.dictionary.git

Install to local repo with
mvn install:install-file -Dfile=lib\cfml.dictionary.jar -DgroupId=cfml.dictionary 
-DartifactId=cfml.dictionary -Dversion=0.0.1 -Dpackaging=jar
mvn install:install-file -Dfile=lib\cfml.parsing.jar -DgroupId=cfml.parsing 
-DartifactId=cfml.parsing -Dversion=0.0.1 -Dpackaging=jar