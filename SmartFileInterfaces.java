package SmartFilePackage;

interface SmartFileReader{
    public void FileReader();
    public void completeFileReader();
}

@FunctionalInterface
interface SmartFileWriter{
    public void FileWriter(String path);
}

@FunctionalInterface 
interface SmartFileDeleter{
    public void FileDeleter(String pat);
}

public interface SmartFileInterfaces extends SmartFileReader, SmartFileWriter, SmartFileDeleter{
}