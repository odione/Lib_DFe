package br.com.dfe.util;

import lombok.extern.log4j.Log4j2;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
class XMLContextFactory {

    private static XMLContextFactory instance = new XMLContextFactory();

    private static final Map<String, JAXBContext> instances = new ConcurrentHashMap<>();

    private XMLContextFactory() {
    }

    /**
     * Returns an existing JAXBContext if one for the particular namespace exists,
     * else it creates an instance adds it to a internal map.
     *
     * @param clazz the context path
     * @return a created JAXBContext
     * @throws JAXBException exception in creating context
     */
    public JAXBContext getJaxBContext(final Class clazz) throws JAXBException {
        JAXBContext context = instances.get(clazz.getName());
        if (context == null) {
            log.debug("Criando novo JAXBContext ("+clazz.getSimpleName()+")");
            context = JAXBContext.newInstance(clazz);
            log.debug("Criado JAXBContext ("+clazz.getSimpleName()+")");
            instances.put(clazz.getName(), context);
        }
        return context;
    }

    <T> Unmarshaller getUnmarshaller(Class<T> clazz) throws JAXBException {
        JAXBContext context = getJaxBContext(clazz);
        return context.createUnmarshaller();
    }

    <T> Marshaller getMarshaller(Class<T> clazz) throws JAXBException {
        JAXBContext context = getJaxBContext(clazz);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
        return marshaller;
    }

    /**
     * Get instance.
     *
     * @return Instance of this factory
     */
    public static XMLContextFactory getInstance() {
        return instance;
    }
}
