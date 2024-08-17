<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="xml" indent="yes" encoding="UTF-8"/>
  <xsl:strip-space elements="*"/>

  <xsl:template match="/">
    <xsl:copy>
      <xsl:apply-templates select="*"/>
    </xsl:copy>
  </xsl:template>

  <xsl:template match="ApplicationData/Loaners/ApplicantDetails">
    <xsl:copy>
      <xsl:apply-templates select="@* | node()"/>
      <INN>772233445566</INN>
    </xsl:copy>
  </xsl:template>

  <xsl:template match="ApplicationData/Loaners/CoApplicantDetails">
    <xsl:copy>
      <xsl:apply-templates select="@* | node()"/>
      <INN>112233445566</INN>
    </xsl:copy>
  </xsl:template>

  <xsl:template match="@* | node()">
    <xsl:copy>
      <xsl:apply-templates select="@* | node()"/>
    </xsl:copy>
  </xsl:template>

</xsl:stylesheet>
