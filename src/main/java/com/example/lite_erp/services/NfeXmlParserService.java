package com.example.lite_erp.services;

import com.example.lite_erp.entities.nfe.DadosNfeDTO;
import com.example.lite_erp.entities.nfe.ItemNfeDTO;
import com.example.lite_erp.entities.nfe.NfeDadosExtraidosDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class NfeXmlParserService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public NfeDadosExtraidosDTO parseXmlNfe(MultipartFile arquivo) throws Exception {
        validarArquivo(arquivo);
        
        try (InputStream inputStream = arquivo.getInputStream()) {
            Document doc = lerXml(inputStream);
            
            String chaveAcesso = extrairChaveAcesso(doc);
            DadosNfeDTO dadosNfe = extrairDadosNfe(doc);
            DadosFornecedorXml dadosFornecedor = extrairDadosFornecedor(doc);
            List<ItemNfeDTO> itens = extrairItens(doc);
            
            return new NfeDadosExtraidosDTO(
                    chaveAcesso,
                    dadosNfe.numeroNota(),
                    dadosNfe.serie(),
                    dadosNfe.dataEmissao(),
                    dadosNfe.valorTotal(),
                    dadosFornecedor.cnpj(),
                    dadosFornecedor.razaoSocial(),
                    dadosFornecedor.nomeFantasia(),
                    itens
            );
        }
    }

    private void validarArquivo(MultipartFile arquivo) {
        if (arquivo == null || arquivo.isEmpty()) {
            throw new IllegalArgumentException("Arquivo XML não pode ser vazio");
        }

        String nomeArquivo = arquivo.getOriginalFilename();
        if (nomeArquivo == null || !nomeArquivo.toLowerCase().endsWith(".xml")) {
            throw new IllegalArgumentException("Arquivo deve ser do tipo XML");
        }

        if (arquivo.getSize() > 10 * 1024 * 1024) {
            throw new IllegalArgumentException("Arquivo XML muito grande. Tamanho máximo: 10MB");
        }
    }

    private Document lerXml(InputStream inputStream) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(inputStream);
    }

    private String extrairChaveAcesso(Document doc) {
        NodeList protNFeList = doc.getElementsByTagName("protNFe");
        if (protNFeList.getLength() > 0) {
            Element protNFe = (Element) protNFeList.item(0);
            NodeList infProtList = protNFe.getElementsByTagName("infProt");
            if (infProtList.getLength() > 0) {
                Element infProt = (Element) infProtList.item(0);
                NodeList chNFeList = infProt.getElementsByTagName("chNFe");
                if (chNFeList.getLength() > 0) {
                    return chNFeList.item(0).getTextContent();
                }
            }
        }
        
        NodeList infNFeList = doc.getElementsByTagName("infNFe");
        if (infNFeList.getLength() > 0) {
            Element infNFe = (Element) infNFeList.item(0);
            String id = infNFe.getAttribute("Id");
            if (id != null && id.startsWith("NFe")) {
                return id.substring(3); // Remove "NFe" do início
            }
        }
        
        return null;
    }

    private DadosNfeDTO extrairDadosNfe(Document doc) {
        Element ide = (Element) doc.getElementsByTagName("ide").item(0);
        Element total = (Element) doc.getElementsByTagName("total").item(0);
        Element icmsTot = (Element) total.getElementsByTagName("ICMSTot").item(0);

        String numeroNota = getTextContent(ide, "nNF");
        String serie = getTextContent(ide, "serie");
        String dataEmissaoStr = getTextContent(ide, "dhEmi");
        
        LocalDate dataEmissao = null;
        if (dataEmissaoStr != null && !dataEmissaoStr.isEmpty()) {
            dataEmissao = LocalDate.parse(dataEmissaoStr.substring(0, 10), DATE_FORMATTER);
        }

        String valorTotalStr = getTextContent(icmsTot, "vNF");
        BigDecimal valorTotal = valorTotalStr != null ? new BigDecimal(valorTotalStr) : BigDecimal.ZERO;

        return new DadosNfeDTO(null, numeroNota, serie, dataEmissao, valorTotal);
    }

    private DadosFornecedorXml extrairDadosFornecedor(Document doc) {
        Element emit = (Element) doc.getElementsByTagName("emit").item(0);

        String cnpj = getTextContent(emit, "CNPJ");
        String razaoSocial = getTextContent(emit, "xNome");
        String nomeFantasia = getTextContent(emit, "xFant");

        return new DadosFornecedorXml(cnpj, razaoSocial, nomeFantasia);
    }

    private List<ItemNfeDTO> extrairItens(Document doc) {
        List<ItemNfeDTO> itens = new ArrayList<>();
        NodeList detList = doc.getElementsByTagName("det");

        for (int i = 0; i < detList.getLength(); i++) {
            Element det = (Element) detList.item(i);
            Element prod = (Element) det.getElementsByTagName("prod").item(0);

            Integer numeroItem = i + 1;
            String codigoProduto = getTextContent(prod, "cProd");
            String descricao = getTextContent(prod, "xProd");
            String ncm = getTextContent(prod, "NCM");
            String ean = getTextContent(prod, "cEAN");
            
            if ("SEM GTIN".equals(ean)) {
                ean = null;
            }

            String quantidadeStr = getTextContent(prod, "qCom");
            BigDecimal quantidade = quantidadeStr != null ? new BigDecimal(quantidadeStr) : BigDecimal.ZERO;

            String unidade = getTextContent(prod, "uCom");

            String valorUnitarioStr = getTextContent(prod, "vUnCom");
            BigDecimal valorUnitario = valorUnitarioStr != null ? new BigDecimal(valorUnitarioStr) : BigDecimal.ZERO;

            String valorTotalStr = getTextContent(prod, "vProd");
            BigDecimal valorTotal = valorTotalStr != null ? new BigDecimal(valorTotalStr) : BigDecimal.ZERO;

            ItemNfeDTO item = new ItemNfeDTO(
                    numeroItem,
                    codigoProduto,
                    descricao,
                    ncm,
                    ean,
                    quantidade,
                    unidade,
                    valorUnitario,
                    valorTotal
            );

            itens.add(item);
        }

        return itens;
    }

    private String getTextContent(Element parent, String tagName) {
        NodeList nodeList = parent.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            String content = nodeList.item(0).getTextContent();
            return content != null && !content.trim().isEmpty() ? content.trim() : null;
        }
        return null;
    }

    private record DadosFornecedorXml(String cnpj, String razaoSocial, String nomeFantasia) {}
}

