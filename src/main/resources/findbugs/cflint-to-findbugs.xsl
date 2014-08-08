<?xml version="1.0" encoding="UTF-8"?>

<!-- CFLint xml to FindBugs xml for HTML output Authors: Ryan Eberly -->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
xmlns:sets="http://exslt.org/sets">

	<xsl:output method="xml" indent="yes" standalone="yes" encoding="UTF-8" />


	<!-- timestamp="1385123483628" analysisTimestamp="1385123483675" -->
	<xsl:template match="/">
		<BugCollection version="0.1.6" sequence="0" release="">
			<Project projectName="">
			</Project>
			<xsl:apply-templates select="/issues/issue" />
			<xsl:call-template name="category"/>
			<xsl:call-template name="pattern"/>
		</BugCollection>
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
				<xsl:value-of select="../@category" />
			</xsl:attribute>
			<xsl:attribute name="priority">
				<xsl:choose>
					<xsl:when test="../@severity = 'ERROR' or ../@severity = 'Error'">1</xsl:when>
					<xsl:when test="../@severity = 'WARNING' or ../@severity = 'Warning'">2</xsl:when>
					<xsl:when test="../@severity = 'INFO' or ../@severity = 'Info'">3</xsl:when>
				</xsl:choose>
			</xsl:attribute>
			<xsl:attribute name="abbrev">
				<xsl:value-of select="../@abbrev" />
			</xsl:attribute>
			<xsl:attribute name="category">
				<xsl:value-of select="'CFLint'" />
			</xsl:attribute>
			<ShortMessage>
				<xsl:value-of select="@message"/>
			</ShortMessage>
			
					<LongMessage><xsl:value-of select="@message"/>
			<xsl:choose>
				<xsl:when test="../@message='MISSING_VAR'">
						<xsl:text>  Use var or the local scope, or otherwise clarify the scope</xsl:text>
				</xsl:when>
			</xsl:choose>
					</LongMessage>

			<Class classname="com.goodville.common.imageright.publish.ImageRightServiceImpl">
				<xsl:attribute name="type">
					<xsl:value-of select="../@category" />
				</xsl:attribute>
				<Message>
					<xsl:text>In </xsl:text>
					<xsl:value-of select="@file"/>
				</Message>
			</Class>
			<xsl:if test="@function">
				<Method> 
					<xsl:attribute name="name">
						<xsl:value-of select="@function" />
					</xsl:attribute>
					<Message>
						<xsl:text>In function </xsl:text>
						<xsl:value-of select="@function"/>
						<xsl:text>()</xsl:text>
					</Message>
				</Method>
			</xsl:if>

			<SourceLine startBytecode="0" endBytecode="0">
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
					<xsl:text>At line : </xsl:text>
					<xsl:value-of select="@line" />
				</Message>
			</SourceLine>
			<xsl:if test="Expression and not(Expression = 'null')">
			<Expression>
				<Message>
					<xsl:text>Expression: </xsl:text>
					<xsl:value-of select="Expression" />
				</Message>
			</Expression>
			</xsl:if>
			
		</BugInstance>
	</xsl:template>

</xsl:stylesheet>

<!-- vim:set ts=4: -->
<!-- Stylus Studio meta-information - (c) 2004-2009. Progress Software Corporation. All rights reserved.

<metaInformation>
	<scenarios>
		<scenario default="yes" name="Scenario1" userelativepaths="yes" externalpreview="no" url="..\..\..\..\..\..\..\..\..\temp\out.xml" htmlbaseurl="" outputurl="" processortype="saxon8" useresolver="yes" profilemode="0" profiledepth="" profilelength=""
		          urlprofilexml="" commandline="" additionalpath="" additionalclasspath="" postprocessortype="none" postprocesscommandline="" postprocessadditionalpath="" postprocessgeneratedext="" validateoutput="no" validator="internal"
		          customvalidator="">
			<advancedProp name="sInitialMode" value=""/>
			<advancedProp name="bXsltOneIsOkay" value="true"/>
			<advancedProp name="bSchemaAware" value="true"/>
			<advancedProp name="bXml11" value="false"/>
			<advancedProp name="iValidation" value="0"/>
			<advancedProp name="bExtensions" value="true"/>
			<advancedProp name="iWhitespace" value="0"/>
			<advancedProp name="sInitialTemplate" value=""/>
			<advancedProp name="bTinyTree" value="true"/>
			<advancedProp name="bWarnings" value="true"/>
			<advancedProp name="bUseDTD" value="false"/>
			<advancedProp name="iErrorHandling" value="fatal"/>
		</scenario>
	</scenarios>
	<MapperMetaTag>
		<MapperInfo srcSchemaPathIsRelative="yes" srcSchemaInterpretAsXML="no" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" destSchemaInterpretAsXML="no"/>
		<MapperBlockPosition></MapperBlockPosition>
		<TemplateContext></TemplateContext>
		<MapperFilter side="source"></MapperFilter>
	</MapperMetaTag>
</metaInformation>
-->