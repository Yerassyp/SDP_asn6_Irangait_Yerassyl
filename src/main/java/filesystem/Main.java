package filesystem;

public class Main {

    public static void main(String[] args) {
        DirectoryNode root = createWorkspace();

        TotalSizeVisitor sizeVisitor = new TotalSizeVisitor();
        sizeVisitor.execute(root);

        System.out.println("Total size: "
                + sizeVisitor.getTotalSize() + " bytes");

        SearchVisitor searchVisitor = new SearchVisitor("java", 150);
        searchVisitor.execute(root);

        System.out.println("\nSearch results:");
        for (String path : searchVisitor.getMatchingPaths()) {
            System.out.println("  " + path);
        }

        StatisticsVisitor statisticsVisitor = new StatisticsVisitor();
        statisticsVisitor.execute(root);

        System.out.println("\nStatistics:");
        System.out.println("Files: " + statisticsVisitor.getFileCount());
        System.out.println("Directories: " + statisticsVisitor.getDirectoryCount());
        System.out.println("Maximum depth: " + statisticsVisitor.getMaxDepth());
    }

    private static DirectoryNode createWorkspace() {
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