package filesystem;

public abstract class AbstractNode implements Element {

    private final String name;
    private DirectoryNode parent;

    protected AbstractNode(String name) {
        if (name == null || name.isBlank()
                || name.equals(".") || name.equals("..")
                || name.contains("/") || name.contains("\\")) {
            throw new IllegalArgumentException("Invalid element name: " + name);
        }

        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    DirectoryNode getParent() {
        return parent;
    }

    void attachTo(DirectoryNode directory) {
        if (directory == null) {
            throw new IllegalArgumentException("Parent cannot be null");
        }

        if (parent != null) {
            throw new IllegalStateException("Element already has a parent");
        }

        parent = directory;
    }
}