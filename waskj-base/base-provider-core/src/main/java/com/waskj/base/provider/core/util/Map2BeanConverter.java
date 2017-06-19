package com.waskj.base.provider.core.util;

import org.nutz.castor.Castors;

import java.util.*;

/**
 * Map转为实体bean
 */
public class Map2BeanConverter {


    public static <T> T convert(Map<String,Object> map,Class<T> clazz){
        Map nestedMap = getNestedMap(map);
        return Castors.me().castTo(nestedMap,clazz);
    }

    private static Map getNestedMap(Map<String,Object> map){
        Collection<TreeNode> treeNodes = convertMapToTrees(map);
        return convertTreesToNestedMap(treeNodes);
    }

    private static Map convertTreesToNestedMap(Collection<TreeNode> treeNodes){
        Map result = new HashMap();

        Map tmp = null;
        for (TreeNode treeNode : treeNodes) {
            tmp = new HashMap();
            nodeToNestedMap(tmp,treeNode);
            result.putAll(tmp);
        }
        return result;
    }

    // 将map中数据转为tree
    private static Collection<TreeNode> convertMapToTrees(Map<String,Object> map) {
        Map<String, TreeNode> rootNodes = new HashMap<String, TreeNode>();
        TreeNode tn = null;
        for (String s : map.keySet()) {
            if (s.indexOf(".") < 0) {
                tn = new TreeNode(s, map.get(s));
                rootNodes.put(s, tn);
            } else {
                String rootPath = s.substring(0, s.indexOf("."));
                if (rootNodes.get(rootPath) == null)
                    rootNodes.put(rootPath, new TreeNode(rootPath));
                tn = rootNodes.get(rootPath);
                String leftPath = s.substring(s.indexOf(".") + 1);

                // let's fill the tree
                fillTree(tn, leftPath, map.get(s));
            }
        }
        return rootNodes.values();
    }

    // 将级联属性，递归成 tree
    private static void fillTree(TreeNode tn,String path,Object value){
        if( path.indexOf(".") < 0 ){
            tn.addChildren(path,value);
            return;
        }

        int ix = path.indexOf(".");
        String first = path.substring(0,ix);
        String left = path.substring(ix + 1);

        TreeNode nextTn = tn.containsChild(first) ? tn.getChild(first) : tn.addChildren(first);
        fillTree(nextTn,left,value);
    }

    private static void nodeToNestedMap(Map emptyMap,TreeNode node){
        if(!node.hasChildren()){
            emptyMap.put(node.getName(),node.getValue());
            return;
        }

        Map newMap = new HashMap();
        emptyMap.put(node.getName(),newMap);

        for (TreeNode tn : node.getChildren()) {
            nodeToNestedMap(newMap,tn);
        }
    }

    // =====================  static class
    static class TreeNode {

        private String name;
        private Object value;
        private Set<TreeNode> children = new HashSet<TreeNode>();

        public TreeNode(){}
        public TreeNode(String name){
            this.name = name;
        }
        public TreeNode(String name,Object value){
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public Object getValue() {
            return value;
        }

        public TreeNode getChild(String name){
            TreeNode tn = null;
            for (TreeNode child : this.children) {
                if( name.equals(child.getName()) ){
                    tn = child;break;
                }
            }
            return tn;
        }

        public TreeNode addChildren(String childName){
            TreeNode tn = new TreeNode(childName);
            this.children.add(tn);
            return tn;
        }

        public TreeNode addChildren(String childName,Object value){
            TreeNode tn = new TreeNode(childName,value);
            this.children.add(tn);
            return tn;
        }

        public boolean containsChild(String name){
            return this.children.contains(new TreeNode(name));
        }

        public Set<TreeNode> getChildren(){
            return Collections.unmodifiableSet(children);
        }

        public boolean hasChildren(){
            return !this.children.isEmpty();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TreeNode treeNode = (TreeNode) o;

            return name.equals(treeNode.name);

        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }


}
