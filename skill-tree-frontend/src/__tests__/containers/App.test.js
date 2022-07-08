import React from "react";
import Adapter from "@wojtekmaj/enzyme-adapter-react-17";
import { shallow, configure } from "enzyme";

import App from "../../containers/App";

configure({ adapter: new Adapter() });

describe("App", () => {
  it("find a div with className `.app`", () => {
    const app = shallow(<App />);

    expect(app.find(".app").length).toBe(1);
  });
});





