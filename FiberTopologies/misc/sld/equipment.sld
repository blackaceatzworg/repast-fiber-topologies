<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" 
 xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd" 
 xmlns="http://www.opengis.net/sld" 
 xmlns:ogc="http://www.opengis.net/ogc" 
 xmlns:xlink="http://www.w3.org/1999/xlink" 
 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <!-- a Named Layer is the basic building block of an SLD document -->
  <NamedLayer>
    <Name>default_point</Name>
    <UserStyle>
	  <FeatureTypeStyle>
        <Rule>
		<Name><![CDATA[Optical Splitter]]></Name>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>entity</ogc:PropertyName>
              <ogc:Literal>Optical Splitter</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
		  <TextSymbolizer>
            <Label>v</Label>
            <Font>
              <CssParameter name="font-family">OSP_A_01</CssParameter>
              <CssParameter name="font-size">12</CssParameter>
              <CssParameter name="font-style">normal</CssParameter>
            </Font>
            <Fill>
              <CssParameter name="fill">#FFFFFF</CssParameter>
            </Fill>         
            <Halo>
              <Radius>2</Radius>
              <Fill>
                <CssParameter name="fill">#CC0000</CssParameter>
              </Fill>
            </Halo>
            <VendorOption name="spaceAround">-1</VendorOption>
          </TextSymbolizer>
        </Rule>
      </FeatureTypeStyle>
	  <FeatureTypeStyle>
        <Rule>
		<Name><![CDATA[Optical Splice]]></Name>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>entity</ogc:PropertyName>
              <ogc:Literal>Optical Splice</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
		  <TextSymbolizer>
            <Label>Ù</Label>
            <Font>
              <CssParameter name="font-family">OSP_A_01</CssParameter>
              <CssParameter name="font-size">10</CssParameter>
              <CssParameter name="font-style">normal</CssParameter>
            </Font>
            <Fill>
              <CssParameter name="fill">#FFFFFF</CssParameter>
            </Fill>         
            <Halo>
              <Radius>2</Radius>
              <Fill>
                <CssParameter name="fill">#FFAA00</CssParameter>
              </Fill>
            </Halo>
            <VendorOption name="spaceAround">-1</VendorOption>
          </TextSymbolizer>
        </Rule>
      </FeatureTypeStyle>
	  <FeatureTypeStyle>
        <Rule>
		<Name><![CDATA[Optical Divider]]></Name>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>entity</ogc:PropertyName>
              <ogc:Literal>Optical Divider</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
		  <TextSymbolizer>
            <Label>W</Label>
            <Font>
              <CssParameter name="font-family">OSP_A_01</CssParameter>
              <CssParameter name="font-size">12</CssParameter>
              <CssParameter name="font-style">normal</CssParameter>
            </Font>
            <Fill>
              <CssParameter name="fill">#FFFFFF</CssParameter>
            </Fill>
            <Halo>
              <Radius>2</Radius>
              <Fill>
                <CssParameter name="fill">#CC0000</CssParameter>
              </Fill>
            </Halo>
            <LabelPlacement>
              <PointPlacement>
                <AnchorPoint>
                  <AnchorPointX>0.5</AnchorPointX>
                  <AnchorPointY>0.0</AnchorPointY>
                </AnchorPoint>
                <Displacement>
                  <DisplacementX>0</DisplacementX>
                  <DisplacementY>10</DisplacementY>
                </Displacement>
              </PointPlacement>
            </LabelPlacement>
            <VendorOption name="spaceAround">-1</VendorOption>
          </TextSymbolizer>
        </Rule>
      </FeatureTypeStyle>
	  <FeatureTypeStyle>
        <Rule>
		<Name><![CDATA[Optical Distribution Point]]></Name>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>entity</ogc:PropertyName>
              <ogc:Literal>Optical Distribution Point</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
		  <TextSymbolizer>
            <Label>À</Label>
            <Font>
              <CssParameter name="font-family">OSP_A_01</CssParameter>
              <CssParameter name="font-size">10</CssParameter>
              <CssParameter name="font-style">normal</CssParameter>
            </Font>
            <Fill>
              <CssParameter name="fill">#FFFFFF</CssParameter>
            </Fill>         
            <Halo>
              <Radius>2</Radius>
              <Fill>
                <CssParameter name="fill">#33DD44</CssParameter>
              </Fill>
            </Halo>
            <VendorOption name="spaceAround">-1</VendorOption>
          </TextSymbolizer>
        </Rule>
      </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>