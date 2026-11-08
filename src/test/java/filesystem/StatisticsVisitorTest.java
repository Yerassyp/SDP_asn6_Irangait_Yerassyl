package filesystem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StatisticsVisitorTest {

    @Test
    void countsFilesDirectoriesAndDepth() {
        DirectoryNode root = createWorkspace();
        StatisticsVisitor visitor = new StatisticsVisitor();

        visitor.execute(root);

        assertEquals(7, visitor.getFileCount());
        assertEquals(6, visitor.getDirectoryCount());
        assertEquals(3, visitor.getMaxDepth());
    }

    @Test
    void emptyDirectoryCountsAsOneDirectory() {
        DirectoryNode root = new DirectoryNode("empty");
        StatisticsVisitor visitor = new StatisticsVisitor();

        visitor.execute(root);

        assertEquals(0, visitor.getFileCount());
        assertEquals(1, visitor.getDirectoryCount());
        assertEquals(0, visitor.getMaxDepth());
    }

    @Test
    void singleFileRootHasDepthZero() {
        FileNode file = new FileNode("report.txt", 100);
        StatisticsVisitor visitor = new StatisticsVisitor();

        visitor.execute(file);

        assertEquals(1, visitor.getFileCount());
        assertEquals(0, visitor.getDirectoryCount());
        assertEquals(0, visitor.getMaxDepth());
    }

    @Test
    void nestedEmptyDirectoriesIncreaseDepth() {
        DirectoryNode root = new DirectoryNode("root");
        DirectoryNode first = new DirectoryNode("first");
        DirectoryNode second = new DirectoryNode("second");

        root.addChild(first);
        first.addChild(second);

        StatisticsVisitor visitor = new StatisticsVisitor();
        visitor.execute(root);

        assertEquals(0, visitor.getFileCount());
        assertEquals(3, visitor.getDirectoryCount());
        assertEquals(2, visitor.getMaxDepth());
    }

    @Test
    void repeatedExecutionResetsStatistics() {
        DirectoryNode root = new DirectoryNode("root");
        root.addChild(new FileNode("a.txt", 120));

        StatisticsVisitor visitor = new StatisticsVisitor();

        visitor.execute(root);
        assertEquals(1, visitor.getFileCount());
        assertEquals(1, visitor.getDirectoryCount());
        assertEquals(1, visitor.getMaxDepth());

        visitor.execute(root);
        assertEquals(1, visitor.getFileCount());
        assertEquals(1, visitor.getDirectoryCount());
        assertEquals(1, visitor.getMaxDepth());

        visitor.execute(new FileNode("b.txt", 50));
        assertEquals(1, visitor.getFileCount());
        assertEquals(0, visitor.getDirectoryCount());
        assertEquals(0, visitor.getMaxDepth());
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