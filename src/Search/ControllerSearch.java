package Search;

import java.util.List;

public class ControllerSearch {

    private final ModelSearch modelSearch;

    public ControllerSearch() {

        this.modelSearch = new ModelSearch();

    }

    public List<String> getDriveList() {

        return modelSearch.getDriveList();

    }

    public void searchForDrives() {

        this.modelSearch.searchForDrives();

    }

}
