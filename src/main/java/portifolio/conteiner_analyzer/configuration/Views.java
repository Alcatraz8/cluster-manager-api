package portifolio.conteiner_analyzer.configuration;

public class Views {

    public static class UserView{}
    public static class CustomerView{}
    public static class ClusterView extends CustomerView{}
    public static class NodeView extends ClusterView{}
    public static class MetricView extends  NodeView{}

}
