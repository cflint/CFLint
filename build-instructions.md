#### Dependencies
CFLint is built with: 
* [Apache Maven](http://maven.apache.org/guides/getting-started/maven-in-five-minutes.html).
           

#### Building CFLint
```
git clone https://github.com/cflint/CFLint.git
cd CFLint
mvn clean install
```
	
#### Building CFParser *(optional)*
Maven will pull the CFParser from the cfparser maven repository on github.  However if you want to build cfparser as well:
```
git clone https://github.com/cfparser/cfparser
cd cfparser
mvn clean install
```

If you have issues, please let us know.		
