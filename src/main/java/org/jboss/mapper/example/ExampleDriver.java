package org.jboss.mapper.example;

import java.io.File;
import java.net.URL;

import org.modeshape.modeler.Metamodel;
import org.modeshape.modeler.Model;
import org.modeshape.modeler.Modeler;

public class ExampleDriver {
    
    private static final String TEST_CONFIGURATION_PATH = "modeler-config.json";
    private static final String TEST_REPOSITORY_STORE_PARENT_PATH = "target/test-repository";
    private static final String METAMODEL_REPOSITORY_URL_PATH = "file:metamodel-repository";
    

    private static final String JAVA_METAMODEL_CATEGORY = "java";
    private static final String JAVA_METAMODEL_ID = "org.modeshape.modeler.java.JavaFile";
    private static final String JAVA_SOURCE_MODEL_PATH = "src/main/java/org/jboss/mapper/example/order/abc/ABCOrder.java";

    private static final String XSD_METAMODEL_CATEGORY = "xsd";
    private static final String XSD_METAMODEL_ID = "org.modeshape.modeler.xsd.Xsd";
    private static final String XSD_MODEL_PATH = "src/main/resources/abc-order.xsd";
    
    private static Modeler modeler;

    public static void main(String[] args) {
        
        try {
            importJavaSourceIntoModel();
            importXSDIntoModel();
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            closeModeler();
        }
    }
    
    public static void importJavaSourceIntoModel() throws Exception {
        initModeler(JAVA_METAMODEL_CATEGORY);
        Metamodel metamodel = modeler.metamodelManager().metamodel(JAVA_METAMODEL_ID);
        Model model = modeler.importModel(new File(JAVA_SOURCE_MODEL_PATH), null, metamodel);
        model.print();
            
    }
    
    public static void importXSDIntoModel() throws Exception {
        initModeler(XSD_METAMODEL_CATEGORY);
        Metamodel metamodel = modeler.metamodelManager().metamodel(XSD_METAMODEL_ID);
        Model model = modeler.importModel(new File(XSD_MODEL_PATH), null, metamodel);
        model.print();
    }
    
    private static void initModeler(String category) throws Exception {
        modeler = Modeler.Factory.instance( TEST_REPOSITORY_STORE_PARENT_PATH, TEST_CONFIGURATION_PATH );
        for ( final URL url : modeler.metamodelManager().metamodelRepositories()) {
            modeler.metamodelManager().unregisterMetamodelRepository(url);
        }
        modeler.metamodelManager().registerMetamodelRepository(new URL(METAMODEL_REPOSITORY_URL_PATH));
        modeler.metamodelManager().install(category);
    }
    
    private static void closeModeler() {
        if (modeler != null) {
            try {
                modeler.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
