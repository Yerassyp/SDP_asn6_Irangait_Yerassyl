package filesystem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TotalSizeVisitorTest {

    @Test
    void sumsFilesAtMultipleDepths() {
        DirectoryNode workspace = createWorkspace();
        TotalSizeVisitor visitor = new TotalSizeVisitor();

        visitor.execute(workspace);

        assertEquals(1000L, visitor.getTotalSize());
    }

    @Test
    void emptyDirectoryHasZeroSize() {
        DirectoryNode empty = new DirectoryNode("empty");
        TotalSizeVisitor visitor = new TotalSizeVisitor();

        visitor.execute(empty);

        assertEquals(0L, visitor.getTotalSize());
    }

    @Test
    void singleFileCanBeRoot() {
        FileNode file = new FileNode("report.txt", 50);
        TotalSizeVisitor visitor = new TotalSizeVisitor();

        visitor.execute(file);

        assertEquals(50L, visitor.getTotalSize());
    }

    @Test
    void repeatedExecutionResetsTotal() {
        DirectoryNode root = new DirectoryNode("root");
        root.addChild(new FileNode("a.txt", 120));

        TotalSizeVisitor visitor = new TotalSizeVisitor();

        visitor.execute(root);
        assertEquals(120L, visitor.getTotalSize());

        visitor.execute(root);
        assertEquals(120L, visitor.getTotalSize());

        visitor.execute(new FileNode("b.txt", 50));
        assertEquals(50L, visitor.getTotalSize());
    }

    private DirectoryNode createWorkspace() {
        DirectoryNode workspace = new DirectoryNode("workspace");

        workspace.addChild(new FileNode("readme.md", 120));
        workspace.addChild(new DirectoryNode("empty"));

        DirectoryNode docs = new DirectoryNode("docs");
        docs.addChild(new FileNode("guide.txt", 200));
        docs.addChild(new FileNode("notes.md", 80));
        workspace.addChild(docs);

        DirectoryNode src = new DirectoryNode("src");
        src.addChild(new FileNode("App.java", 300));

        DirectoryNode utils = new DirectoryNode("utils");
        utils.addChild(new FileNode("FileHelper.java", 150));
        utils.addChild(new FileNode("AppTest.java", 90));

        src.addChild(utils);
        workspace.addChild(src);

        DirectoryNode archive = new DirectoryNode("archive");
        archive.addChild(new FileNode("notes-old.md", 60));
        workspace.addChild(archive);

        return workspace;
    }
}