package filesystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class SearchVisitor implements Visitor {

    private final String nameContains;
    private final long minimumSize;
    private final List<String> matchingPaths = new ArrayList<>();

    private Element root;
    private String currentPath = "";

    public SearchVisitor(String nameContains, long minimumSize) {
        this.nameContains = Objects.requireNonNull(
                nameContains, "Name criterion cannot be null");

        if (minimumSize < 0) {
            throw new IllegalArgumentException("Minimum size cannot be negative");
        }

        this.minimumSize = minimumSize;
    }

    public void execute(Element root) {
        this.root = Objects.requireNonNull(root, "Root cannot be null");

        matchingPaths.clear();
        currentPath = "";

        root.accept(this);
        matchingPaths.sort(String::compareTo);
    }

    @Override
    public void visit(FileNode file) {
        if (file.getName().contains(nameContains)
                && file.getSizeBytes() >= minimumSize) {

            String relativePath = currentPath.isEmpty()
                    ? file.getName()
                    : currentPath + "/" + file.getName();

            matchingPaths.add(relativePath);
        }
    }

    @Override
    public void visit(DirectoryNode directory) {
        String previousPath = currentPath;

        if (directory != root) {
            currentPath = currentPath.isEmpty()
                    ? directory.getName()
                    : currentPath + "/" + directory.getName();
        }

        for (Element child : directory.getChildren()) {
            child.accept(this);
        }

        currentPath = previousPath;
    }

    public List<String> getMatchingPaths() {
        return List.copyOf(matchingPaths);
    }
}