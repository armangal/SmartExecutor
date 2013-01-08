package org.smexec.configuration;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "SmartExecutor", namespace = "")
@XmlAccessorType(XmlAccessType.FIELD)
public class Config {

    @XmlElement(name = "pool")
    @XmlElementWrapper
    private List<PoolConfiguration> pools;

    public List<PoolConfiguration> getPools() {
        return pools;
    }

    public void setPools(List<PoolConfiguration> pools) {
        this.pools = pools;
    }

    public PoolConfiguration getPoolConfiguration(String poolName) {
        for (PoolConfiguration pc : pools) {
            if (pc.getPoolName().equals(poolName)) {
                return pc;
            }
        }
        return null;
    }

    public static void main(String[] args)
        throws JAXBException {
        JAXBContext contextA = JAXBContext.newInstance(Config.class);

        Marshaller marshaller = contextA.createMarshaller();

        Config config = new Config();
        List<PoolConfiguration> pools = new ArrayList<PoolConfiguration>();
        config.setPools(pools);
        for (int i = 0; i < 3; i++) {
            PoolConfiguration pc = new PoolConfiguration();
            pc.setCorePollSize(i + 1);
            pc.setMaxPoolSize(5);
            pc.setPoolName("Name1" + i);
            pc.setPoolNameShort("Name1Sho" + i);
            pc.setPoolType(PoolType.regular);
            pc.setQueueSize(100);
            pc.setKeepAliveTime(1000l);

            pools.add(pc);
        }
        marshaller.marshal(config, System.out);
    }
}
