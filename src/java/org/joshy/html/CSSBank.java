package org.joshy.html;



import java.util.List;

import java.util.ArrayList;

import java.util.Iterator;

import java.io.*;

import java.awt.Color;

import org.joshy.html.css.*;



import com.steadystate.css.*;

import com.steadystate.css.parser.*;

import org.w3c.dom.*;

import org.w3c.dom.css.*;

import org.w3c.css.sac.*;



import org.joshy.u;

import org.joshy.x;

// CLEAN
import com.pdoubleya.xhtmlrenderer.css.XRStyleSheet;
import java.net.*;
import org.apache.xpath.XPathAPI;


public class CSSBank extends CSSAccessor {

    /* internal vars */

    
    /* CLN: removed (PWW 13/08/04)*/
    //private List sheets;

    /* CLN: removed (PWW 13/08/04)*/
    //private List style_nodes;

    /* CLN: public to private (PWW 13/08/04)*/
    private List styles;

    /* CLN: package-public to private (PWW 13/08/04)*/
    private CSSParser parser;

    /* CLN: public to private (PWW 13/08/04)*/
    private RuleBank rule_bank;

    public CSSBank() {
      this(new RuleFinder());
    }

    // Instantiates for a specific RuleBank
    public CSSBank(RuleBank rb) {
        styles = new ArrayList();

        rule_bank = rb;

        parser = new CSSParser(this.rule_bank);
    }


    public void parse(Reader reader) throws IOException {

        parser.parse(reader);

    }

    // HACK: origin flag is new feature in XRStyleReference, added here for
    // common interface, though it is ignored in CSSBank
    public void parse(Reader reader, int origin) throws IOException {

        parser.parse(reader);

    }

    public void parse(String reader) throws IOException {

        parser.parse(reader);

    }

    /**
     * Same as {@link #parse(Reader, int)} for a String datasource.
     *
     * @param source           A String containing CSS style rules
     * @param origin           See {@link #parse(Reader, int)}
     * @exception IOException  {@link #parse(Reader, int)}
     */
    // HACK: origin flag is new feature in XRStyleReference, added here for
    // common interface, though it is ignored in CSSBank
    public void parse( String source, int origin )
    throws IOException {
        parse(source);   
    }

    /**
     * Parses the CSS style information from a "<link>" Elements (for example in
     * XHTML), and loads these rules into the associated RuleBank.
     *
     * @param root             Root of the document for which to search for link tags.
     * @exception IOException  Throws
     */
    public void parseLinkedStyles( Element root )
    throws IOException {
        try {
            NodeList nl = XPathAPI.selectNodeList(root, "//link[@type='text/css']/@href");
            for ( int i=0, len=nl.getLength(); i < len; i++ ) {
                Node hrefNode = nl.item(i);
                String href = hrefNode.getNodeValue();
                URL url = new URL(href);
                InputStream is = url.openStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                InputStreamReader reader = new InputStreamReader(bis);
                parse(reader, XRStyleSheet.AUTHOR);
                is.close();
            }
        } catch ( Exception ex ) {
            ex.printStackTrace();   
        }
    }
        
        
    public void parseInlineStyles(Element elem) throws IOException {

        parser.parseInlineStyles(elem);

    }

    /**
     * Parses the CSS style information from the inline "style" attribute on the
     * DOM Element, and loads these rules into the associated RuleBank,
     * automatically associating those styles as matched to the Element.
     *
     * @param elem             The Element from which to pull a style attribute.
     * @exception IOException  Throws
     */
    public void parseElementStyling( Element elem )
    throws IOException {
      
      // TODO: code parsing for Element style attribute
      throw new RuntimeException ("NOT CODED: pulling style attributes from an Element."); 
    }

  /**
   * <p>
   *
   * Attempts to match any loaded to Elements in the supplied Document, using
   * CSS2 matching rules on selection. This should be called after all
   * stylesheets and styles are loaded, but before any properties are retrieved.
   * </p>
   *
   * @param document  PARAM
   */
   public void matchStyles( Document document ) {
      System.out.println("!! CSSBank does not currently implement anything in matchStyles()."); 
   }
  
    private void pullOutStyles(CSSStyleSheet sheet) throws IOException {

        parser.pullOutStyles(sheet);

    }



    

    /* ========= property accessors ============ */

    private Object getProperty(Node node, String prop) {

        if(node.getNodeType() == node.TEXT_NODE) {

            return getProperty(node.getParentNode(),prop);

        }

        if(node.getNodeType() == node.ELEMENT_NODE) {

            return getProperty((Element)node,prop);

        }

        u.p("unknown node type: " + node);

        u.p("type = " + node.getNodeType());

        return null;

    }

    

    public CSSValue getProperty(Element elem, String prop, boolean inherit) {

        if(elem.getNodeName().equals("a") && prop.equals("color")) {

            //u.p("looking at: " + elem.getNodeName() + " prop = " + prop + " inherit " + inherit);

        }

        //RuleFinder rf = new RuleFinder(this.styles);

        CSSStyleDeclaration style_dec = rule_bank.findRule(elem,prop,inherit);

        if(elem.getNodeName().equals("a") && prop.equals("color")) {

            //u.p("got style: " + style_dec);

        }

        if(style_dec == null) {

            //u.p("print there is no style declaration at all for: " + elem.getNodeName());

            //u.p("looking for property: " + prop);

            return null;

        }

        CSSValue val = style_dec.getPropertyCSSValue(prop);
        
        if(val == null) {

            u.p("WARNING!! elem " + elem.getNodeName() + " doesn't have the property: " + prop);

            /*

            if(elem.getParentNode() != null && inherit) {

                u.p("going up: " + elem.getNodeName() + " -> " + elem.getParentNode().getNodeName() + " prop = " + prop);

                if(elem.getParentNode() instanceof Element) {

                    return getProperty((Element)elem.getParentNode(),prop,inherit);

                }

            }

            return null;

            */

        }

        //u.p("returning: " + val);

        return val;

    }

    

    

    



}

