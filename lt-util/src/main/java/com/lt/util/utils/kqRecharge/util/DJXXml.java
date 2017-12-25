package com.lt.util.utils.kqRecharge.util;

import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;



public class DJXXml {

	//解析tr2报文
	public NodeList Jxml(String protocolXML){
		
		NodeList list = null;
		try {   
            DocumentBuilderFactory factory = DocumentBuilderFactory   
                    .newInstance();   
            DocumentBuilder builder = factory.newDocumentBuilder();   
            org.w3c.dom.Document doc = builder.parse(new InputSource(new StringReader(protocolXML)));   
            
            org.w3c.dom.Element root = doc.getDocumentElement();   
            NodeList nodeList = root.getChildNodes(); 
            list = nodeList;
            System.out.println(nodeList.getLength());
           if (nodeList != null) {   
               for (int i = 0; i < nodeList.getLength(); i++) {   
                    Node node = nodeList.item(i);   
                    System.out.println("节点=" + node.getNodeName() + "\ttext="  
                            + node.getFirstChild().getNodeValue());   
                }   
            } 
           
        } catch (Exception e) {   
            e.printStackTrace();   
        }   
		return list;
		
	}
	
	
	
	public static void main(String[] args) {
		DJXXml doc = new DJXXml();
		String xml = "<?xml version='1.0' encoding='UTF-8' standalone='yes'?><MasMessage xmlns='http://www.99bill.com/mas_cnp_merchant_interface'><version>1.0</version><TxnMsgContent><txnType>PUR</txnType><interactiveStatus>TR2</interactiveStatus><amount>2222</amount><merchantId>610124198907167</merchantId><terminalId>19890737</terminalId><entryTime>20151014161220</entryTime><externalRefNumber>2018918162222</externalRefNumber><transTime>20151015165247</transTime><refNumber>000012457503</refNumber><responseCode>00</responseCode><responseTextMessage>交易成功</responseTextMessage><cardOrg>CU</cardOrg><issuer>中国银行</issuer><storableCardNo>4380880007</storableCardNo><authorizationCode>170014</authorizationCode></TxnMsgContent></MasMessage>";
        String xml2 = "<TxnMsgContent><txnType>PUR</txnType><amount>100</amount><merchantId>812051545110011</merchantId><terminalId>00006034</terminalId><entryTime>20151023145222</entryTime><externalRefNumber>20151023145222</externalRefNumber><transTime>20151023145224</transTime><voidFlag>0</voidFlag><refNumber>000012580338</refNumber><responseCode>00</responseCode><responseTextMessage>交易成功</responseTextMessage><cardOrg>CU</cardOrg><issuer>中国银行</issuer><storableCardNo>4380880007</storableCardNo><authorizationCode>147079</authorizationCode><txnStatus>S</txnStatus></TxnMsgContent>";
        //doc.ceshi(xml);
        //System.out.print(doc.xmlElements(xml).get(0));
        doc.Jxml(xml2);
	}

}
