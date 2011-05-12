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
		<Name><![CDATA[Tower]]></Name>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>entity</ogc:PropertyName>
              <ogc:Literal>Tower</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
		  <TextSymbolizer>
            <Label>r</Label>
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
                <CssParameter name="fill">#BBBBBB</CssParameter>
              </Fill>
            </Halo>
            <VendorOption name="spaceAround">-1</VendorOption>
          </TextSymbolizer>
        </Rule>
      </FeatureTypeStyle>
	  <FeatureTypeStyle>
        <Rule>
		<Name><![CDATA[Technical Underground Gallery]]></Name>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>entity</ogc:PropertyName>
              <ogc:Literal>Technical Underground Gallery</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
		  <TextSymbolizer>
            <Label>x</Label>
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
                <CssParameter name="fill">#BBBBBB</CssParameter>
              </Fill>
            </Halo>
            <VendorOption name="spaceAround">-1</VendorOption>
          </TextSymbolizer>
        </Rule>
      </FeatureTypeStyle>
	  <FeatureTypeStyle>
        <Rule>
		<Name><![CDATA[Technical Building]]></Name>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>entity</ogc:PropertyName>
              <ogc:Literal>Technical Building</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
		  <TextSymbolizer>
            <Label>q</Label>
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
                <CssParameter name="fill">#BBBBBB</CssParameter>
              </Fill>
            </Halo>
            <VendorOption name="spaceAround">-1</VendorOption>
          </TextSymbolizer>
        </Rule>
      </FeatureTypeStyle>
	  <FeatureTypeStyle>
        <Rule>
		<Name><![CDATA[Pole]]></Name>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>entity</ogc:PropertyName>
              <ogc:Literal>Pole</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
		  <TextSymbolizer>
            <Label>m</Label>
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
                <CssParameter name="fill">#BBBBBB</CssParameter>
              </Fill>
            </Halo>
            <VendorOption name="spaceAround">-1</VendorOption>
          </TextSymbolizer>
        </Rule>
      </FeatureTypeStyle>
	  <FeatureTypeStyle>
        <Rule>
		<Name><![CDATA[Manhole]]></Name>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>entity</ogc:PropertyName>
              <ogc:Literal>Manhole</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
		  <TextSymbolizer>
            <Label>c</Label>
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
                <CssParameter name="fill">#BBBBBB</CssParameter>
              </Fill>
            </Halo>
            <VendorOption name="spaceAround">-1</VendorOption>
          </TextSymbolizer>
        </Rule>
      </FeatureTypeStyle>
	  <FeatureTypeStyle>
        <Rule>
		<Name><![CDATA[Equipment Cabinet]]></Name>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>entity</ogc:PropertyName>
              <ogc:Literal>Equipment Cabinet</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
		  <TextSymbolizer>
            <Label>}</Label>
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
                <CssParameter name="fill">#BBBBBB</CssParameter>
              </Fill>
            </Halo>
            <VendorOption name="spaceAround">-1</VendorOption>
          </TextSymbolizer>
        </Rule>
      </FeatureTypeStyle>
	  <FeatureTypeStyle>
        <Rule>
		<Name><![CDATA[Central Office]]></Name>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>entity</ogc:PropertyName>
              <ogc:Literal>Central Office</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
		  <TextSymbolizer>
            <Label>4</Label>
            <Font>
              <CssParameter name="font-family">OSP_A_01</CssParameter>
              <CssParameter name="font-size">24</CssParameter>
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
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>