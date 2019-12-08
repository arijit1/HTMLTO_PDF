package itextConverterStyle;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.net.FileRetrieve;
import com.itextpdf.tool.xml.net.FileRetrieveImpl;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.AbstractImageProvider;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import com.itextpdf.tool.xml.pipeline.html.LinkProvider;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ConvertHtmlToPdf {

	// public static final String RELATIVE_PATH =
	// "C:\\Users\\zzz\\Desktop\\itext\\";

	public static final String HTML = "C:\\Users\\htmlwithStyle.html";
	public static final String DEST = "E://HTMLtoPDFWithStyle.pdf";
	public static final String CSSPath = "C:\\Users\\style";
	public static final String IMG_PATH = "C:\\Users\\";

	
	public void createPdf(String file) throws IOException, DocumentException {

		Document document = new Document();

		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));

		document.open();

		CSSResolver cssResolver = XMLWorkerHelper.getInstance().getDefaultCssResolver(false);
		FileRetrieve retrieve = new FileRetrieveImpl(CSSPath);
		System.out.println(retrieve);
		cssResolver.setFileRetrieve(retrieve);

		HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);
		htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
		htmlContext.setImageProvider(new AbstractImageProvider() {
			public String getImageRootPath() {
				return IMG_PATH;
			}
		});
		
		 /*htmlContext.setLinkProvider(new LinkProvider() { public String
		 getLinkRoot() { return RELATIVE_PATH; } });
		 */
		// Pipelines
		PdfWriterPipeline pdf = new PdfWriterPipeline(document, writer);
		HtmlPipeline html = new HtmlPipeline(htmlContext, pdf);
		CssResolverPipeline css = new CssResolverPipeline(cssResolver, html);

		// XML Worker
		XMLWorker worker = new XMLWorker(css, true);
		XMLParser p = new XMLParser(worker);
		p.parse(new FileInputStream(HTML));

		// step 5
		document.close();
	}

	public static void main(String[] args) throws IOException, DocumentException {
		// TODO code application logic here
		new ConvertHtmlToPdf().createPdf(DEST);
	}

}