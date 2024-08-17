•	<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="xml" indent="yes"/>
  <xsl:strip-space elements="*"/>
  <xsl:template match="/">
    <xsl:copy>
      <xsl:apply-templates select="*"/>
    </xsl:copy>
  </xsl:template>

  <xsl:template match="ApplicationData/Loaners">
    <xsl:copy>
      <xsl:apply-templates select="*"/>
      <GuarantorDetails>
        <FullName>Петров Петр Петрович</FullName>
        <DateOfBirth>1980-01-10</DateOfBirth>
        <ClientID>98765</ClientID>
      </GuarantorDetails>
    </xsl:copy>
  </xsl:template>

  <xsl:template match="@* | node()">
    <xsl:copy>
      <xsl:apply-templates select="@* | node()"/>
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>
