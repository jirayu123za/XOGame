package xo.demo.Controller;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;
import java.util.*;

// Controller class for handling HTTP requests and rendering the XO game page.
@Controller
@RequestMapping("/")
public class GameController{
    /**
     * Renders the XO game page with an empty board.
     * @return the model and view for the XO game page
     */
    @GetMapping
    public ModelAndView position(){
        return game();
    }

    @RequestMapping("/position")
    public ModelAndView game(){
        ModelAndView modelAndView = new ModelAndView("position");
        String[][] board = new String[3][3];
        Arrays.stream(board).forEach(row -> Arrays.fill(row, " "));
        modelAndView.addObject("board", board);
        return modelAndView;
    }
}