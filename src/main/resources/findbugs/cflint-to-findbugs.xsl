<?xml version="1.0" encoding="UTF-8"?>

<!-- CFLint XML to FindBugs XML (https://github.com/findbugsproject/findbugs/blob/master/findbugs/etc/bugcollection.xsd) -->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output method="xml" indent="yes" standalone="yes" encoding="UTF-8" omit-xml-declaration="yes"/>

	<xsl:template match="/">
		<xsl:variable name="version" select="/issues/@version"/>
		<xsl:variable name="timestamp" select="/issues/@timestamp"/>

		<BugCollection sequence="0" release="">
			<xsl:attribute name="timestamp">
				<xsl:value-of select="$timestamp"/>
			</xsl:attribute>
			<xsl:attribute name="analysisTimestamp">
				<xsl:value-of select="$timestamp"/>
			</xsl:attribute>
			<xsl:attribute name="version">
				<xsl:value-of select="$version"/>
			</xsl:attribute>
			<Project projectName=""/>
			<xsl:apply-templates select="/issues/issue" />
			<xsl:call-template name="category" />
			<xsl:call-template name="pattern" />
			<Errors/>
			<xsl:call-template name="summary" />
			<ClassFeatures/>
			<History/>
		</BugCollection>
	</xsl:template>

	<xsl:template name="summary">
		<FindBugsSummary timestamp="" num_packages="0">
			<xsl:attribute name="timestamp">
				<xsl:value-of select="/issues/@timestamp"/>
			</xsl:attribute>
			<xsl:attribute name="total_bugs">
				<xsl:value-of select="sum(/issues/counts/count[@code]/@count)"/>
			</xsl:attribute>
			<xsl:attribute name="total_classes">
				<xsl:value-of select="/issues/counts/@totalfiles"/>
			</xsl:attribute>
			<xsl:attribute name="total_size">
				<xsl:value-of select="/issues/counts/@totallines"/>
			</xsl:attribute>
			<xsl:attribute name="priority_1">
				<xsl:value-of select="count(/issues/issue[@severity = 'FATAL' or @severity = 'Fatal' or @severity = 'CRITICAL' or @severity = 'Critical'])"/>
			</xsl:attribute>
			<xsl:attribute name="priority_2">
				<xsl:value-of select="count(/issues/issue[@severity = 'ERROR' or @severity = 'Error' or @severity = 'WARNING' or @severity = 'Warning' or @severity = 'CAUTION' or @severity = 'Caution' ])"/>
			</xsl:attribute>
			<xsl:attribute name="priority_3">
				<xsl:value-of select="count(/issues/issue[@severity = 'INFO' or @severity = 'Info' or @severity = 'COSMETIC' or @severity = 'Cosmetic'])"/>
			</xsl:attribute>
			<xsl:for-each select="/issues/issue/location[@file]">
				<xsl:if test="count(preceding::*[./@file = current()/@file]) = 0">
					<FileStats>
						<xsl:attribute name="path">
							<xsl:value-of select="@file" />
						</xsl:attribute>
						<xsl:attribute name="bugCount">
							<xsl:value-of select="count(//@file[.=current()/@file])" />
						</xsl:attribute>
					</FileStats>
				</xsl:if>
			</xsl:for-each>

		</FindBugsSummary>
	</xsl:template>

	<xsl:template name="category">
		<xsl:for-each select="/issues/issue[@category]">
			<xsl:variable name="cat" select="@category"/>
			<xsl:if test="not(preceding-sibling::issue[@category=$cat])">
				<BugCategory>
					<xsl:attribute name="category">
						<xsl:value-of select="$cat"/>
					</xsl:attribute>
					<Description>
						<xsl:value-of select="$cat"/>
					</Description>
				</BugCategory>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>

	<xsl:template name="pattern">
		<xsl:for-each select="/issues/issue[@message]">
			<xsl:variable name="msg" select="@message"/>
			<xsl:if test="not(preceding-sibling::issue[@message=$msg])">
				<xsl:for-each select="/issues/issue[@message=$msg][1]">
					<BugPattern>
						<xsl:attribute name="abbrev">
							<xsl:value-of select="@abbrev"/>
						</xsl:attribute>
						<xsl:attribute name="category">
							<xsl:value-of select="@category"/>
						</xsl:attribute>
						<xsl:attribute name="type">
							<xsl:value-of select="@message"/>
						</xsl:attribute>
						<ShortDescription><xsl:value-of select="@message"/></ShortDescription>
						<Details>
							<xsl:choose>
								<xsl:when test="@message='MISSING_VAR'">
									<xsl:text>Variable assigned without a scope.  This puts in a large scope that is usually intended.  Suggest using the 'var name' statement of explicit 'LOCAL.name' statement.</xsl:text>
								</xsl:when>
								<xsl:when test="@message='NESTED_CFOUTPUT'">
									<xsl:text>Nested &lt;cfquery/&gt; tags, the outer tag has an @query attribute, it should also specify the @group attribute.</xsl:text>
								</xsl:when>
								<xsl:when test="@message='QUERYNEW_DATATYPE'">
									<xsl:text>QueryNew should specify the datatypes of the columns.</xsl:text>
								</xsl:when>
								<xsl:when test="@message='ARG_VAR_CONFLICT'">
									<xsl:text>Variable is varr'd with the same name as one of the arguments.  This is confusing or incorrect.</xsl:text>
								</xsl:when>
								<xsl:when test="@message='ARG_VAR_MIXED'">
									<xsl:text>Variable referenced both as an unscoped (local) and an argument.</xsl:text>
								</xsl:when>
								<xsl:when test="@message='ARG_DEFAULT_MISSING'">
									<xsl:text>Arguments that are not required should specify a default value (@default)</xsl:text>
								</xsl:when>
								<xsl:when test="@message='OUTPUT_ATTR'">
									<xsl:text>Functions should specify @output="false"</xsl:text>
								</xsl:when>
							</xsl:choose>
						</Details>
					</BugPattern>
				</xsl:for-each>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>

	<xsl:template match="issue/location">
		<BugInstance>
			<xsl:attribute name="type">
				<xsl:value-of select="../@id" />
			</xsl:attribute>
			<xsl:attribute name="priority">
				<xsl:choose>
					<xsl:when test="../@severity = 'FATAL' or ../@severity = 'Fatal'">1</xsl:when>
					<xsl:when test="../@severity = 'CRITICAL' or ../@severity = 'Critical'">2</xsl:when>
					<xsl:when test="../@severity = 'ERROR' or ../@severity = 'Error'">3</xsl:when>
					<xsl:when test="../@severity = 'WARNING' or ../@severity = 'Warning'">4</xsl:when>
					<xsl:when test="../@severity = 'CAUTION' or ../@severity = 'Caution'">5</xsl:when>
					<xsl:when test="../@severity = 'INFO' or ../@severity = 'Info'">6</xsl:when>
					<xsl:when test="../@severity = 'COSMETIC' or ../@severity = 'Cosmetic'">7</xsl:when>
				</xsl:choose>
			</xsl:attribute>
			<xsl:attribute name="abbrev">
				<xsl:value-of select="../@abbrev" />
			</xsl:attribute>
			<xsl:attribute name="category">
				<xsl:value-of select="../@category" />
			</xsl:attribute>
			<ShortMessage>
				<xsl:value-of select="@message"/>
			</ShortMessage>
			<LongMessage>
				<xsl:value-of select="@message"/>
				<xsl:choose>
					<xsl:when test="../@message='MISSING_VAR'">
						<xsl:text>  Use var or the local scope, or otherwise clarify the scope</xsl:text>
					</xsl:when>
				</xsl:choose>
			</LongMessage>
			<Class>
				<xsl:attribute name="classname">
					<xsl:value-of select="@file" />
				</xsl:attribute>
				<SourceLine startBytecode="0" endBytecode="0">
					<xsl:attribute name="classname">
						<xsl:value-of select="@file" />
					</xsl:attribute>
					<xsl:attribute name="start">
						<xsl:value-of select="@line" />
					</xsl:attribute>
					<xsl:attribute name="end">
						<xsl:value-of select="@line" />
					</xsl:attribute>
					<xsl:attribute name="sourcefile">
						<xsl:value-of select="@fileName" />
					</xsl:attribute>
					<xsl:attribute name="sourcepath">
						<xsl:value-of select="@file" />
					</xsl:attribute>
					<Message>
						<xsl:text>At line: </xsl:text>
						<xsl:value-of select="@line" />
						<xsl:text>, at column: </xsl:text>
						<xsl:value-of select="@column" />
					</Message>
				</SourceLine>
			</Class>
			<xsl:if test="@function and string-length(@function)!=0">
				<Method signature="" isStatic="false">
					<xsl:attribute name="classname">
						<xsl:value-of select="@file" />
					</xsl:attribute>
					<xsl:attribute name="name">
						<xsl:value-of select="@function" />
					</xsl:attribute>
					<SourceLine startBytecode="0" endBytecode="0">
						<xsl:attribute name="classname">
							<xsl:value-of select="@file" />
						</xsl:attribute>
						<xsl:attribute name="start">
							<xsl:value-of select="@line" />
						</xsl:attribute>
						<xsl:attribute name="end">
							<xsl:value-of select="@line" />
						</xsl:attribute>
						<xsl:attribute name="sourcefile">
							<xsl:value-of select="@fileName" />
						</xsl:attribute>
						<xsl:attribute name="sourcepath">
							<xsl:value-of select="@file" />
						</xsl:attribute>
						<Message>
							<xsl:text>In function </xsl:text>
							<xsl:value-of select="@function"/>
							<xsl:text>()</xsl:text>
						</Message>
					</SourceLine>
				</Method>
			</xsl:if>

			<xsl:if test="Expression and not(Expression = 'null')">
			<LocalVariable register="0" pc="0" role="">
				<xsl:attribute name="name">
					<xsl:value-of select="Expression" />
				</xsl:attribute>
				<Message>
					<xsl:text>Expression: </xsl:text>
					<xsl:value-of select="Expression" />
				</Message>
			</LocalVariable>
			</xsl:if>
			
		</BugInstance>
	</xsl:template>

</xsl:stylesheet>
