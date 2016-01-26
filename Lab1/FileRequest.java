class FileRequest {
	String username;
	String filePath;
	int fileSize;

	FileRequest(String _username, String _filePath, int _fileSize) {
		username = _username;
		filePath = _filePath;
		fileSize = _fileSize;
	}
}