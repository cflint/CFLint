<?xml version="1.0" encoding="UTF-8"?>
<!--
  CFLint HTML Report - style: default
  Based on plain.xsd from FindBugs
-->

<!--
  FindBugs - Find bugs in Java programs
  Copyright (C) 2004,2005 University of Maryland
  Copyright (C) 2005, Chris Nappin

  This library is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public
  License as published by the Free Software Foundation; either
  version 2.1 of the License, or (at your option) any later version.

  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General Public
  License along with this library; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
-->
<xsl:stylesheet version="1.0"
                xmlns="http://www.w3.org/1999/xhtml"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output
            method="xml"
            omit-xml-declaration="yes"
            standalone="yes"
            doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"
            doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
            indent="yes"
            encoding="UTF-8"/>

    <xsl:variable name="bugTableHeader">
        <tr class="tableheader">
            <th align="left">Warning</th>
            <th align="left">Severity</th>
            <th align="left">Details</th>
        </tr>
    </xsl:variable>

    <xsl:template match="/">
        <html>
            <head>
                <title>CFLint Report</title>
                <style type="text/css">
                    .tablerow0 {
                    background: #EEEEEE;
                    }

                    .tablerow1 {
                    background: white;
                    }

                    .detailrow0 {
                    background: #EEEEEE;
                    }

                    .detailrow1 {
                    background: white;
                    }

                    .tableheader {
                    background: #b9b9fe;
                    font-size: larger;
                    }

                    p {
                    margin-top: 0px;
                    }
                </style>
            </head>

            <xsl:variable name="unique-patternkey" select="/BugCollection/BugPattern/@type"/>

            <body>

                <h1>CFLint Report</h1>
                <p>Produced using <a href="https://github.com/cflint/CFLint">CFLint</a>&#160;(<xsl:value-of select="/BugCollection/@version"/>).</p>
                <xsl:if test='string-length(/Project/@projectName)>0'><p>Project: <xsl:value-of select="/Project/@projectName" /></p></xsl:if>

                <h2>Metrics</h2>
                <xsl:apply-templates select="/BugCollection/FindBugsSummary"/>

                <h2>Summary</h2>
                <table width="500" cellpadding="5" cellspacing="2">
                    <tr class="tableheader">
                        <th align="left">Warning Type</th>
                        <th align="right">Number</th>
                    </tr>

                    <xsl:for-each select="$unique-patternkey">
                        <xsl:sort select="." order="ascending"/>
                        <xsl:variable name="patternkey" select="."/>
                        <xsl:variable name="patterndesc" select="/BugCollection/BugPattern[@type=$patternkey]/ShortDescription"/>
                        <xsl:variable name="styleclass">
                            <xsl:choose><xsl:when test="position() mod 2 = 1">tablerow0</xsl:when>
                                <xsl:otherwise>tablerow1</xsl:otherwise>
                            </xsl:choose>
                        </xsl:variable>

                        <tr class="{$styleclass}">
                            <td><a href="#Warnings_{$patternkey}"><xsl:value-of select="$patterndesc"/> Warnings</a></td>
                            <td align="right"><xsl:value-of select="count(/BugCollection/BugInstance[(@type=$patternkey) and (not(@last))])"/></td>
                        </tr>
                    </xsl:for-each>

                    <xsl:variable name="styleclass">
                        <xsl:choose><xsl:when test="count($unique-patternkey) mod 2 = 0">tablerow0</xsl:when>
                            <xsl:otherwise>tablerow1</xsl:otherwise>
                        </xsl:choose>
                    </xsl:variable>
                    <tr class="{$styleclass}">
                        <td><b>Total</b></td>
                        <td align="right"><b><xsl:value-of select="count(/BugCollection/BugInstance[not(@last)])"/></b></td>
                    </tr>
                </table>
                <p><br/><br/></p>

                <h1>Warnings</h1>

                <p>Click on each warning link to see a full description of the issue, and
                    details of how to resolve it.</p>

                <xsl:for-each select="$unique-patternkey">
                    <xsl:sort select="." order="ascending"/>
                    <xsl:variable name="patternkey" select="."/>
                    <xsl:variable name="patterndesc" select="/BugCollection/BugPattern[@type=$patternkey]/ShortDescription"/>
                    <xsl:call-template name="generateWarningTable">
                        <xsl:with-param name="warningSet" select="/BugCollection/BugInstance[(@type=$patternkey) and (not(@last))]"/>
                        <xsl:with-param name="sectionTitle"><xsl:value-of select="$patterndesc"/> Warnings</xsl:with-param>
                        <xsl:with-param name="sectionId">Warnings_<xsl:value-of select="$patternkey"/></xsl:with-param>
                    </xsl:call-template>
                </xsl:for-each>

                <p><br/><br/></p>
                <h1><a name="Details">Warning Types</a></h1>

                <xsl:apply-templates select="/BugCollection/BugPattern">
                    <xsl:sort select="@abbrev"/>
                    <xsl:sort select="ShortDescription"/>
                </xsl:apply-templates>

            </body>
        </html>
    </xsl:template>

    <xsl:template match="BugInstance[not(@last)]">
        <xsl:variable name="warningId"><xsl:value-of select="generate-id()"/></xsl:variable>

        <tr class="tablerow{position() mod 2}">
            <td width="20%" valign="top">
                <a href="#{@type}"><xsl:value-of select="ShortMessage"/></a>
            </td>
            <td width="10%" valign="top">
                <xsl:choose>
                    <!-- HIGH PRIORITY -->
                    <xsl:when test="@priority = 1">Fatal</xsl:when>
                    <xsl:when test="@priority = 2">Critical</xsl:when>

                    <!-- MEDIUM PRIORITY -->
                    <xsl:when test="@priority = 3">Error</xsl:when>
                    <xsl:when test="@priority = 4">Warning</xsl:when>
                    <xsl:when test="@priority = 5">Caution</xsl:when>

                    <!-- LOW PRIORITY -->
                    <xsl:when test="@priority = 6">Info</xsl:when>
                    <xsl:when test="@priority = 7">Cosmetic</xsl:when>

                    <xsl:otherwise>Unknown</xsl:otherwise>
                </xsl:choose>
            </td>
            <td width="70%">
                <p>
                    <!-- add source filename and line number(s), if any -->
                    <xsl:if test="Class/SourceLine">
                        <xsl:value-of select="Class/SourceLine/@sourcepath"/>,
                        <xsl:choose>
                            <xsl:when test="Class/SourceLine/@start = Class/SourceLine/@end">
                                line <xsl:value-of select="Class/SourceLine/@start"/>
                            </xsl:when>
                            <xsl:otherwise>
                                lines <xsl:value-of select="Class/SourceLine/@start"/>
                                to <xsl:value-of select="Class/SourceLine/@end"/>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:if>

                    <xsl:for-each select="./descendant::Message">
                        <br/><br/><xsl:value-of select="text()"/>
                    </xsl:for-each>
                </p>
            </td>
        </tr>
    </xsl:template>

    <xsl:template match="BugPattern">
        <h2><a name="{@type}"><xsl:value-of select="ShortDescription"/></a></h2>
        <xsl:value-of select="Details" disable-output-escaping="yes"/>
        <p><br/><br/></p>
    </xsl:template>

    <xsl:template name="generateWarningTable">
        <xsl:param name="warningSet"/>
        <xsl:param name="sectionTitle"/>
        <xsl:param name="sectionId"/>

        <h2><a name="{$sectionId}"><xsl:value-of select="$sectionTitle"/></a></h2>
        <table class="warningtable" width="100%" cellspacing="2" cellpadding="5">
            <xsl:copy-of select="$bugTableHeader"/>
            <xsl:choose>
                <xsl:when test="count($warningSet) &gt; 0">
                    <xsl:apply-templates select="$warningSet">
                        <xsl:sort select="@priority"/>
                        <xsl:sort select="@abbrev"/>
                        <xsl:sort select="Class/@classname"/>
                    </xsl:apply-templates>
                </xsl:when>
                <xsl:otherwise>
                    <tr><td colspan="2"><p><i>None</i></p></td></tr>
                </xsl:otherwise>
            </xsl:choose>
        </table>
        <p><br/><br/></p>
    </xsl:template>

    <xsl:template match="FindBugsSummary">
        <xsl:variable name="kloc" select="@total_size div 1000.0"/>
        <xsl:variable name="format" select="'#######0.00'"/>

        <p><xsl:value-of select="@total_size"/> lines of code analyzed,
            in <xsl:value-of select="@total_classes"/> files</p>
        <table width="500" cellpadding="5" cellspacing="2">
            <tr class="tableheader">
                <th align="left">Metric</th>
                <th align="right">Total</th>
                <th align="right">Density*</th>
            </tr>
            <tr class="tablerow0">
                <td>High Priority Warnings</td>
                <td align="right"><xsl:value-of select="@priority_1"/></td>
                <td align="right">
                    <xsl:choose>
                        <xsl:when test= "number($kloc) &gt; 0.0">
                            <xsl:value-of select="format-number(@priority_1 div $kloc, $format)"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="format-number(0.0, $format)"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </td>
            </tr>
            <tr class="tablerow1">
                <td>Medium Priority Warnings</td>
                <td align="right"><xsl:value-of select="@priority_2"/></td>
                <td align="right">
                    <xsl:choose>
                        <xsl:when test="number($kloc) &gt; 0.0">
                            <xsl:value-of select="format-number(@priority_2 div $kloc, $format)"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="format-number(0.0, $format)"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </td>
            </tr>
            <tr class="tablerow0">
                <td>Low Priority Warnings</td>
                <td align="right"><xsl:value-of select="@priority_3"/></td>
                <td align="right">
                    <xsl:choose>
                        <xsl:when test="number($kloc) &gt; 0.0">
                            <xsl:value-of select="format-number(@priority_3 div $kloc, $format)"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="format-number(0.0, $format)"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </td>
            </tr>
            <tr class="tablerow1">
                <td><b>Total Warnings</b></td>
                <td align="right"><b><xsl:value-of select="@total_bugs"/></b></td>
                <td align="right">
                    <b>
                        <xsl:choose>
                            <xsl:when test="number($kloc) &gt; 0.0">
                                <xsl:value-of select="format-number(@total_bugs div $kloc, $format)"/>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:value-of select="format-number(0.0, $format)"/>
                            </xsl:otherwise>
                        </xsl:choose>
                    </b>
                </td>
            </tr>
        </table>
        <p><i>(* Defects per Thousand lines of non-commenting source statements)</i></p>
        <p><br/><br/></p>

    </xsl:template>

</xsl:stylesheet>