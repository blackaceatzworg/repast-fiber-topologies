<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" 
    xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd" 
    xmlns="http://www.opengis.net/sld" 
    xmlns:ogc="http://www.opengis.net/ogc" 
    xmlns:xlink="http://www.w3.org/1999/xlink" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <NamedLayer>
    <Name>route-styling</Name>
    <UserStyle>
      <Title>Route SLD</Title>
      <FeatureTypeStyle>
        <Rule>
          <Name><![CDATA[Physical Infrastructure Network]]></Name>
          <LineSymbolizer>
            <Stroke>
              <CssParameter name="stroke">#0099EE</CssParameter>
              <CssParameter name="stroke-width">1</CssParameter>
              <CssParameter name="stroke-linecap">round</CssParameter>
              <!--<CssParameter name="stroke-opacity">1</CssParameter>-->
            </Stroke>
          </LineSymbolizer>
        </Rule>
      </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>