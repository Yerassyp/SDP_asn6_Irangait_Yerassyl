package filesystem;

import java.util.Objects;

public final class TotalSizeVisitor implements Visitor {

    private long totalSize;

    public void execute(Element root) {
        Objects.requireNonNull(root, "Root cannot be null");

        totalSize = 0;
        root.accept(this);
    }

    @Override
    public void visit(FileNode file) {
        totalSize = Math.addExact(totalSize, file.getSizeBytes());
    }

    @Override
    public void visit(DirectoryNode directory) {
        for (Element child : directory.getChildren()) {
            child.accept(this);
        }
    }

    public long getTotalSize() {
        return totalSize;
    }
}