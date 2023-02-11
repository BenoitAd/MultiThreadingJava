package sujet;

import java.util.Objects;

public class CustomNode {
    public String linkToParse;

    public CustomNode(String linkToParse) {
        this.linkToParse = linkToParse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomNode that = (CustomNode) o;
        return linkToParse.equals(that.linkToParse);
    }

}