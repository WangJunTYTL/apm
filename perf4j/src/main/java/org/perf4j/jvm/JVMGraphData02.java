package org.perf4j.jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjun on 16/1/21.
 */
public class JVMGraphData02 {

    public String tag;// thread memory gc
    public List<String> counts = new ArrayList<String>();
    public List<String> labels = new ArrayList<String>();

    public JVMGraphData02(String tag){
        this.tag = tag;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JVMGraphData02)) return false;

        JVMGraphData02 that = (JVMGraphData02) o;

        return tag != null ? tag.equals(that.tag) : that.tag == null;

    }

    @Override
    public int hashCode() {
        return tag != null ? tag.hashCode() : 0;
    }
}
