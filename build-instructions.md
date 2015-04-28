#### Dependencies
CFLint is built with: 
* [Apache Maven](http://maven.apache.org/guides/getting-started/maven-in-five-minutes.html).
           

#### Building CFLint
```
git clone https://github.com/cflint/CFLint.git
cd CFLint
mvn clean install
```

The build artifacts can be found in *target/appassembler*.  

`mvn clean install -D assembleDirectory=/usr/local` can be used to install the artifacts directly into the PATH of most UNIX-like systems.
	
#### Building CFParser *(optional)*
Maven will pull cfparser from maven central repository.  However if you want to build cfparser separately see https://github.com/cfparser/cfparser

If you have issues, please let us know.		
