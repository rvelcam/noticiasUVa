package com.example.nylon.noticias_teleco;

        import android.os.AsyncTask;
        import android.util.Log;

        import org.w3c.dom.Document;
        import org.w3c.dom.Element;
        import org.w3c.dom.Node;
        import org.w3c.dom.NodeList;

        import java.io.InputStream;
        import java.net.HttpURLConnection;
        import java.net.URL;
        import java.util.ArrayList;
        import java.util.HashMap;

        import javax.xml.parsers.DocumentBuilder;
        import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Héctor on 30/11/2015.
 */
public class Asincrono extends AsyncTask<Void, Void, ArrayList<HashMap<String, String>>> {
    ArrayList<HashMap<String, String>> listaNoticias2 = new ArrayList<>();
    Boolean finalizado;
    String primera_noticia;


    @Override
    protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
        String direccion = "http://www.tel.uva.es/rss/tablon.xml";
        ArrayList<HashMap<String, String>> listaNoticias = new ArrayList<>();
        try {
            URL url = new URL(direccion);
            HttpURLConnection conexion = (HttpURLConnection)url.openConnection();
            conexion.setRequestMethod("GET");
            InputStream inputStream = conexion.getInputStream();
            Log.d("","Vamos a procesar el XML");
            listaNoticias = procesarXML(inputStream);
            Log.d("","Procesado!");
            //Log.d("","Comprobamos procesado: "+listaNoticias.get(0).get("title"));
        } catch (Exception e) {
            Log.d("","CAAAAAATCH");
        }
        return listaNoticias;
    }


    //HAcer un metodo De finalizado hilo con un bolean
    public Boolean finalizado(){ return finalizado;}


    @Override
    protected void onPostExecute(ArrayList<HashMap<String,String>> listaNoticias){
        Log.d("","Asincrono finalizado");
        Log.d("","1"+listaNoticias.get(0).get("title"));
        listaNoticias2=listaNoticias;
        Log.d("","2"+listaNoticias2.get(0).get("title"));
        finalizado = true;
    }

    public ArrayList<HashMap<String,String>> getListaNoticias(){
        return listaNoticias2;
    }

    public String getPrimeraNoticia(){ return primera_noticia;}

    public ArrayList<HashMap<String, String>> procesarXML(InputStream inputStream) throws Exception {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
        Document xmlDocument = documentBuilder.parse(inputStream);
        Element rootElement = xmlDocument.getDocumentElement();
        Log.d("", rootElement.getTagName());
        NodeList itemsList = rootElement.getElementsByTagName("item");
        NodeList itemChildren;
        Node itemActual = null;
        Node childActual = null;


        HashMap<String, String> actualMap = null;
        ArrayList<HashMap<String, String>> listaNoticias = new ArrayList<>();

        for(int i = 0; i<itemsList.getLength();i++){
            itemActual = itemsList.item(i);
            itemActual.getChildNodes();
            itemChildren = itemActual.getChildNodes();

            actualMap = new HashMap<>();
            for(int j = 0; j<itemChildren.getLength();j++){
                childActual = itemChildren.item(j);
                if(childActual.getNodeName().equalsIgnoreCase("title")){
                    //Log.d("",childActual.getTextContent());
                    actualMap.put("title",childActual.getTextContent());
                    if(i==0){
                        primera_noticia = childActual.getTextContent();
                    }
                }
                if(childActual.getNodeName().equalsIgnoreCase("link")){
                    //Log.d("",childActual.getTextContent());
                    actualMap.put("link",childActual.getTextContent());
                }
                if(childActual.getNodeName().equalsIgnoreCase("description")){
                    // Log.d("",childActual.getTextContent());
                    actualMap.put("descripcion",childActual.getTextContent());
                }
                if(childActual.getNodeName().equalsIgnoreCase("category")){
                    //Log.d("",childActual.getTextContent());
                    actualMap.put("category",childActual.getTextContent());
                }
                if(childActual.getNodeName().equalsIgnoreCase("pubDate")){
                    //Log.d("",childActual.getTextContent());
                    actualMap.put("pubDate",childActual.getTextContent());
                }

            }
            if(actualMap!=null && !actualMap.isEmpty()){
                listaNoticias.add(actualMap);
            }
        }

        return listaNoticias;
    }
}