package afro.xmltree.xmlrenderer;

import afro.xmltree.Attribute;
import afro.xmltree.Leaf;
import afro.xmltree.LeafList;

/**
 *
 * @author dmn
 */
public class XmlRenderer implements XmlRendererInterface {

    @Override
    public String render(Leaf root) {
        return renderImpl(root, "");
    }

    @Override
    public String render(LeafList list) {
        StringBuilder result = new StringBuilder();
        for (Leaf item : list) {
            result.append(render(item));
        }
        return result.toString();
    }

    static int escapeCounter = 0;

    private static String escapeString(String s) {
//        System.out.println((escapeCounter++) +" " + s);
        int rawopenidx = -1, rawcloseidx = -1;
        String rawopendata = null, rawclosedata = null;
        int idx = -1;
        //if (false)
        for (int i = 0; i < RAWOPEN.length; i++) {
            rawopendata = RAWOPEN[i];
            rawclosedata = RAWCLOSE[i];
            rawopenidx = s.indexOf(rawopendata);
            rawcloseidx = s.indexOf(rawclosedata, rawopenidx + 1);
            if (rawopenidx >= 0 && rawcloseidx > 0) {
                idx = i;
                break;
            }
        }
        if (idx >= 0) {
            String s1 = s.substring(0, rawopenidx);
            String s2 = s.substring(rawopenidx, rawcloseidx + rawclosedata.length());
            String s3 = s.substring(rawcloseidx + rawclosedata.length());
            s1 = escapeString(s1);
            s3 = escapeString(s3);
            return (s1 + s2 + s3);
        }
        return (s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("\'", "&apos;"));//.trim();
    }

    private String renderImpl(Leaf root, String indent) {
        StringBuilder result = new StringBuilder();
        result.append(indent);
        boolean dataLeaf = root.isDataLeaf();
//        if ("content".equals(root.getName())) return "";
//        if ("source".equals(root.getName())) return "";
//        if ("summary".equals(root.getName())) return "";
        if (!dataLeaf) {
            result.append(OPEN);
            result.append(root.getName());
        }
        for (Attribute attribute : root.getAttributes()) {
            result.append(SP);
            result.append(attribute.getKey());
            result.append(EQ);
            result.append(QUOT);
            result.append(escapeString(attribute.getValue()));
            result.append(QUOT);
        }
        if (root.getValue() == null && root.getChildren().isEmpty()) {
            result.append(SLASH);
            result.append(CLOSE);
        } else {
            if (!dataLeaf) {
                result.append(CLOSE);
            }
            if (root.getValue() != null) {
                result.append(escapeString(root.getValue()));//.replaceAll("\n", "|"));
            }
            if (!root.getChildren().isEmpty()) {
                if (root.getChildren().size() == 1 && root.getChildren().get(0).isDataLeaf()) {
                    result.append(escapeString(root.getChildren().get(0).getValue()));
                } else {
                    boolean first = true;
                    for (Leaf item : root.getChildren()) {
                        if (first && item.isDataLeaf()) {
                            result.append(renderImpl(item, ""));
                                //result.append("###"+CR);
                        } else {
                            if (first) {
                                result.append(CR);
                            }
                            result.append(renderImpl(item, TAB + indent));
                        }
                        first = false;
                    }
                    if (!dataLeaf) {
                        result.append(indent);
                    }
                }
            }
            if (!dataLeaf) {
                result.append(OPEN);
                result.append(SLASH);
                result.append(root.getName());
                result.append(CLOSE);
            }
        }
        //result.append("#"+root.getChildren().size());
        result.append(CR);
        return result.toString();
    }
}
