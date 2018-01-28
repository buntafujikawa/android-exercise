package com.example.buntafujikawa.rssreader.parser

import com.example.buntafujikawa.rssreader.data.Link
import com.example.buntafujikawa.rssreader.data.Site
import org.w3c.dom.Document
import org.w3c.dom.NodeList
import org.xml.sax.SAXException
import java.io.IOException
import java.io.InputStream
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException

/**
 * RSSの入力ストリームを受け取って、SiteとList<Link>を生成する
 */

class RssParser {

    private lateinit var site: Site
    private lateinit var links: List<Link>

    // RSSフィードの入力ストリームを解析する
    fun parse(input: InputStream): Boolean {
        val dbFactory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
        dbFactory.isNamespaceAware = false

        try {
            val builder: DocumentBuilder = dbFactory.newDocumentBuilder()
            val document: Document = builder.parse(input)
            input.close()

            // RSSバージョンから適切なパーサーを得る
            val parser: FeedParser? = getParser(document)

            if (parser != null && parser.parse(document)) {
                this.site = parser.getSite()
                this.links = parser.getLinkList()

                return true
            }

        } catch (e: ParserConfigurationException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: SAXException) {
            e.printStackTrace()
        }

        return false
    }

    fun getSite(): Site {
        return site
    }

    fun getLinkList(): List<Link> {
        return links
    }

    private fun getParser(document: Document): FeedParser? {
        val children: NodeList = document.childNodes
        var parser: FeedParser? = null

        for (i in 0 until children.length) {
            var childName: String = children.item(i).nodeName

            parser = when {
                childName.equals("rdf:RDF") -> { Rss1Parser() }
                childName.equals("rss") -> { Rss2Parser() }
                else -> null
            }
        }

        return parser
    }
}
