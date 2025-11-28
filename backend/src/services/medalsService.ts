/**
 * Medals Service
 * Developer: Hawkaye Visions LTD — Pakistan
 */

interface Medal {
  id: string;
  name: string;
  category: string;
  description: string;
  icon: string;
  earnedAt: number | null;
  isDisplayed: boolean;
}

interface MedalsResult {
  medals: Medal[];
  displaySettings: {
    displayedMedals: string[];
    hiddenMedals: string[];
    maxDisplayed: number;
  };
}

// Mock medals data
const mockMedals: Medal[] = [
  {
    id: 'gift_sender_3',
    name: 'Gift Sender III',
    category: 'gift',
    description: 'Send 1M coins in gifts',
    icon: 'medal_gift_sender_3.png',
    earnedAt: Date.now() - 7 * 24 * 60 * 60 * 1000,
    isDisplayed: true
  },
  {
    id: 'login_30d',
    name: '30 Day Veteran',
    category: 'activity',
    description: 'Log in for 30 cumulative days',
    icon: 'medal_login_30d.png',
    earnedAt: Date.now() - 14 * 24 * 60 * 60 * 1000,
    isDisplayed: true
  },
  {
    id: 'level_20',
    name: 'Established',
    category: 'achievement',
    description: 'Reach Level 20',
    icon: 'medal_level_20.png',
    earnedAt: Date.now() - 21 * 24 * 60 * 60 * 1000,
    isDisplayed: true
  }
];

export const getUserMedals = async (userId: string): Promise<MedalsResult> => {
  return {
    medals: mockMedals,
    displaySettings: {
      displayedMedals: mockMedals.filter(m => m.isDisplayed).map(m => m.id),
      hiddenMedals: mockMedals.filter(m => !m.isDisplayed).map(m => m.id),
      maxDisplayed: 10
    }
  };
};

export const updateMedalDisplay = async (
  userId: string,
  displayedMedals: string[],
  hiddenMedals: string[]
): Promise<void> => {
  // Update medal display settings in database
};
