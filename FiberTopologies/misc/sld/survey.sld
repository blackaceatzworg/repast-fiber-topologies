<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" 
 xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd" 
 xmlns="http://www.opengis.net/sld" 
 xmlns:ogc="http://www.opengis.net/ogc" 
 xmlns:xlink="http://www.w3.org/1999/xlink" 
 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <!-- a Named Layer is the basic building block of an SLD document -->
  <NamedLayer>
    <Name>Survey</Name>
    <UserStyle>
      <FeatureTypeStyle>
        <Rule>
		<Name><![CDATA[Survey styling]]></Name>
		  <TextSymbolizer>
		  	<!-- <Label>A</Label>-->
            <Label><![CDATA[½]]></Label>
            <Font>
              <CssParameter name="font-family">OSP_A_01</CssParameter>
              <CssParameter name="font-size">15</CssParameter>
              <CssParameter name="font-style">normal</CssParameter>
            </Font>
            <Fill>
              <CssParameter name="fill">#FFFFFF</CssParameter>
            </Fill>         
            <Halo>
              <Radius>2</Radius>
              <Fill>
                <CssParameter name="fill">#3300BB</CssParameter>
              </Fill>
            </Halo>
            <VendorOption name="spaceAround">-1</VendorOption>
          </TextSymbolizer>
        </Rule>
      </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>